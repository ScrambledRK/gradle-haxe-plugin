package at.dotpoint.gradle.haxe

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.util.StringUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.haxe.sourceset.HaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSetInternal
import at.dotpoint.gradle.haxe.specification.HaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpecInternal
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.TaskContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.BinaryContainer
import org.gradle.platform.base.BinarySpec
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
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
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
		 * @param binarySpec
		 */
		@Mutate
		void generateBinaryTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			for( BinarySpec binary : binaries )
			{
				if ( !(binary instanceof IApplicationBinarySpec) )
					continue;

				IApplicationBinarySpec binarySpec = (IApplicationBinarySpec) binary;

				// ------------- //

				List<String> cycles = [CrossPlugin.NAME_CONVERT_SOURCE, CrossPlugin.NAME_COMPILE_SOURCE ];

				cycles.each { String cycle ->

					String cycleName 	= binarySpec.tasks.taskName( cycle );
					String generateName = StringUtil.toCamelCase( "generate", cycleName, "hxml" );
					String executeName 	= StringUtil.toCamelCase( "execute", cycleName, "hxml" );

					// ---------------- //

					Task cycleTask 		= TaskUtil.findTaskByName( binarySpec.tasks, cycleName );

					Task generateTask 	= TaskUtil.createBinaryTask( binarySpec, GenerateHXMLTask.class, generateName );
					Task executeTask 	= TaskUtil.createBinaryTask( binarySpec, ExecuteHXMLTask.class, executeName );

					// ---------------- //

					executeTask.dependsOn generateTask;
					cycleTask.dependsOn executeTask;
				}
			}
		}

	}
}
