package at.dotpoint.gradle.haxe

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.util.StringUtil
import at.dotpoint.gradle.haxe.sourceset.HaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSetInternal
import at.dotpoint.gradle.haxe.specification.HaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpecInternal
import at.dotpoint.gradle.haxe.task.ConvertHaxeSourceTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
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

		// -------------------------------------------------- //
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

		// -------------------------------------------------- //
		// -------------------------------------------------- //

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

			String nameTaskConvert 	= StringUtil.toCamelCase( CrossPlugin.NAME_TRANSPILE_SOURCE, nameScoped );
			String nameTaskCompile 	= StringUtil.toCamelCase( CrossPlugin.NAME_COMPILE_SOURCE, nameScoped );

			// ------------- //

			Task buildTask = null;

			binarySpec.tasks.create( nameTaskConvert, ConvertHaxeSourceTask.class )
			{
				ISourceSetInternal sourceSet = (ISourceSetInternal)(applicationComponentSpec.sources.get(CrossPlugin.NAME_COMPILE_SOURCE));

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

	}
}
