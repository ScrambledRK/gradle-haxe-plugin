package at.dotpoint.gradle.haxe

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.util.StringUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolverRepository
import at.dotpoint.gradle.haxe.sourceset.HaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSetInternal
import at.dotpoint.gradle.haxe.specification.HaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpecInternal
import at.dotpoint.gradle.haxe.task.ConvertHaxeSourceTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.tasks.TaskContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.language.base.ProjectSourceSet
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.model.Each
import org.gradle.model.Finalize
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.platform.base.BinaryContainer
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.ComponentBinaries
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

import javax.inject.Inject
import javax.management.modelmbean.ModelMBean

/**
 *  Created by RK on 28.03.2016.
 */
class HaxePlugin implements Plugin<Project>
{

	public static final String NAME_TRANSPILE_SOURCE 	= "transpile";
	public static final String NAME_COMPILE_SOURCE 		= "compile";
	public static final String NAME_CONVERT_ASSETS 		= "convert";

	/**
	 *
	 */
	public final Instantiator instantiator;
	public final FileResolver fileResolver;

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	@Inject
	public HaxePlugin( Instantiator instantiator, FileResolver fileResolver )
	{
		this.instantiator = instantiator;
		this.fileResolver = fileResolver;
	}

	@Override
	public void apply( final Project project )
	{
		project.getPluginManager().apply( CrossPlugin.class );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 */
	@SuppressWarnings("UnusedDeclaration")
	//
	static class HaxePluginRules extends RuleSource
	{

		/**
		 * HaxeComponentSpec
		 */
		@ComponentType
		void registerHaxeBinarySpec( TypeBuilder<IHaxeBinarySpec> builder )
		{
			builder.defaultImplementation( HaxeBinarySpec.class );
			builder.internalView( IHaxeBinarySpecInternal.class );
		}

		// -------------------------------------------------- //gr
		// -------------------------------------------------- //
		// source transformation:

		/**
		 * LanguageSourceSet
		 */
		@ComponentType
		void registerSourceSet( TypeBuilder<IHaxeSourceSet> builder )
		{
			builder.defaultImplementation( HaxeSourceSet.class );
			builder.internalView( IHaxeSourceSetInternal.class );
		}

//		@Mutate
//		void registerLanguageTransform( LanguageTransformContainer languages, ServiceRegistry serviceRegistry )
//		{
//			ModelSchemaStore schemaStore = serviceRegistry.get( ModelSchemaStore.class );
//			languages.add( new HaxeLanguageTransform() );
//		}

		@Mutate
		void removeDefaultTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			tasks.create( HaxePlugin.NAME_TRANSPILE_SOURCE, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "converts non-native sources to the native target language of a binary"
			}

			tasks.create( HaxePlugin.NAME_COMPILE_SOURCE, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "compiles native sources to a native target binary"
			}

			tasks.create( HaxePlugin.NAME_CONVERT_ASSETS, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "converts raw assets for the native target binary"
			}

			tasks.getByName(HaxePlugin.NAME_COMPILE_SOURCE).dependsOn HaxePlugin.NAME_TRANSPILE_SOURCE;

			// -------------- //

			for( BinarySpec binary : binaries )
			{
				for( Task task in binary.getTasks() )
				{
					if( task.name.startsWith(HaxePlugin.NAME_TRANSPILE_SOURCE) )
						tasks.getByName(HaxePlugin.NAME_TRANSPILE_SOURCE).dependsOn task;

					if( task.name.startsWith(HaxePlugin.NAME_COMPILE_SOURCE) )
						tasks.getByName(HaxePlugin.NAME_COMPILE_SOURCE).dependsOn task;

					if( task.name.startsWith(HaxePlugin.NAME_CONVERT_ASSETS) )
						tasks.getByName(HaxePlugin.NAME_CONVERT_ASSETS).dependsOn task;
				}
			}
		}

		/**
		 *
		 * @param task
		 */
		@Finalize
		void removeAssembleDependencies( @Path("tasks.assemble") Task task )
		{
			(DefaultTaskDependency)(task.getTaskDependencies()).getValues().clear();
			task.setDependsOn( [ HaxePlugin.NAME_COMPILE_SOURCE, HaxePlugin.NAME_CONVERT_ASSETS ] );
		}

		/**
		 *
		 * @param builder
		 * @param applicationComponentSpec
		 * @param variantResolver
		 */
		@Mutate
		void generateBinaryTasks( @Each IApplicationBinarySpec binarySpec )
		{
			IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;
			IApplicationComponentSpecInternal applicationComponentSpec = (IApplicationComponentSpecInternal) binarySpec.application;

			String nameScoped = binarySpecInternal.getProjectScopedName();

			String nameTaskConvert 	= StringUtil.toCamelCase( HaxePlugin.NAME_TRANSPILE_SOURCE, nameScoped );
			String nameTaskCompile 	= StringUtil.toCamelCase( HaxePlugin.NAME_COMPILE_SOURCE, nameScoped );

			// ------------- //

			Task buildTask = null;

			binarySpec.tasks.create( nameTaskConvert, ConvertHaxeSourceTask.class )
			{
				ISourceSetInternal sourceSet = (ISourceSetInternal)(applicationComponentSpec.sources.get(HaxePlugin.NAME_COMPILE_SOURCE));

				it.inputPlatform = sourceSet.sourcePlatform;
				it.outputPlatform = binarySpecInternal.targetPlatform;
			}

			binarySpec.tasks.create( nameTaskCompile, GenerateHXMLTask.class )
			{
				it.dependsOn nameTaskConvert;
				buildTask = it;
			}

			binarySpec.setBuildTask( buildTask );
		}

		/**
		 *
		 */
		private <TTask extends DefaultTask> TTask createTask( Project project, Class<TTask> task, String name )
		{
			return (TTask)(project.getTasks().create( name, task ));
		}
	}
}
