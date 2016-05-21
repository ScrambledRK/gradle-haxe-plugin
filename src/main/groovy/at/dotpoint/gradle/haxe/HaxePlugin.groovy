package at.dotpoint.gradle.haxe

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec
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
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.ModelMap
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentBinaries
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

import javax.inject.Inject
/**
 *  Created by RK on 28.03.2016.
 */
class HaxePlugin implements Plugin<Project>
{

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

		/**
		 *
		 * @param builder
		 * @param applicationComponentSpec
		 * @param variantResolver
		 */
		@ComponentBinaries
		void generateApplicationBinaries( ModelMap<IApplicationBinarySpec> builder, IApplicationComponentSpec applicationComponentSpec )
		{
			println " > > > " + applicationComponentSpec.name;

			builder.each { IApplicationBinarySpec binarySpec ->

				IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;

				println( "  " + binarySpec.name );

				binarySpec.tasks.create( binarySpecInternal.getProjectScopedName() + "Convert", ConvertHaxeSourceTask.class )
				{

				}

				binarySpec.tasks.create( binarySpecInternal.getProjectScopedName() + "Hxml", GenerateHXMLTask.class )
				{

				}

				binarySpec.tasks.each {
					println "     " + it.name;
				}
			}
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
