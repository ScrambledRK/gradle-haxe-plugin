package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.ACrossTransform
import at.dotpoint.gradle.cross.transform.ACrossTransformTaskConfig
import at.dotpoint.gradle.cross.transform.ICrossTransformTaskConfig
import at.dotpoint.gradle.cross.util.StringUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
/**
 * Created by RK on 27.02.16.
 */
class HaxeLanguageTransform extends ACrossTransform<ISourceSet, ISourceSet>
{

	@Override
	public ICrossTransformTaskConfig getTransformTask()
	{
		return new HaxeSourceTransformTaskConfig();
	}

	@Override
	public boolean applyToBinary( IApplicationBinarySpec binary )
	{
		return true;
	}

	// -------------------------------------- //
	// -------------------------------------- //

	/**
	 *
	 */
	public static class HaxeSourceTransformTaskConfig extends ACrossTransformTaskConfig
	{

		/**
		 * in case multiple sourceSets are attached to a binarySpec you'll be asked if you can transform the other ones as well
		 *
		 * @param candidate
		 * @return
		 */
		@Override
		boolean canTransform( ISourceSet candidate )
		{
			return candidate.sourcePlatform.name == "haxe";
		}

		/**
		 * actually create the sourceSet transformation tasks
		 *
		 * @param parent LifeCycleTask of the BinarySpec; convert or compile; dependsOn will be set for newly created task
		 * @param cycle LifeCycle name; convert or compile
		 * @param binarySpec variation permutation to transform (convert, compile)
		 * @param sourceSet one of possible many source sets
		 * @param serviceRegistry in case you need some fancy new classes
		 * @param isJoint false - first run for the first sourceSet, true - followup runs for other sourceSets
		 * @return sourceSet specific convert or compile task
		 */
		@Override
		Task createSourceSetCycleTask( Task parent, String cycle, IApplicationBinarySpec binarySpec, ISourceSet sourceSet,
									   ServiceRegistry serviceRegistry, boolean isJoint )
		{
			if( isJoint && cycle == "compile" )
				return null;

			String cycleName 	= binarySpec.tasks.taskName( cycle );
			String generateName = StringUtil.toCamelCase( "generate", cycleName, "hxml", sourceSet.name );
			String executeName 	= StringUtil.toCamelCase( "execute", cycleName, "hxml", sourceSet.name );

			// ---------------- //

			Task generateTask 	= TaskUtil.createBinaryTask( binarySpec, sourceSet, GenerateHXMLTask.class, generateName );
			Task executeTask 	= TaskUtil.createBinaryTask( binarySpec, sourceSet, ExecuteHXMLTask.class, executeName );

			executeTask.dependsOn generateTask;

			return executeTask;
		}
	}
}