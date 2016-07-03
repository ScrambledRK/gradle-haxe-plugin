package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.transform.AConvertTransform
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.iterator.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 27.02.16.
 */
class HaxeConvertTransform extends AConvertTransform
{

	@Override
	protected boolean isValidInputSourceSet(ISourceSet sourceSet) {
		return true
	}

	@Override
	protected boolean isValidTargetPlatform(IPlatform platform) {
		return true
	}

	@Override
	protected boolean isValidTargetFlavor(IFlavor platform) {
		return false
	}

	/**
	 * actually creates all required tasks to convert a given SourceSet to the requested target variations. these
	 * tasks are not necessarily unique for each BinarySpec but are in combination of SourceSet and target variations.
	 *
	 * @param sourceSet SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @param taskContainer container to create the tasks in, beware of naming
	 * @return top-level task to depend the conversion LifeCycle step on
	 */
	@Override
	Task createTransformTask( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation, TaskContainer taskContainer )
	{
		Task generateTask 	= TaskUtil.createTransformTask( "generateHxml", GenerateHXMLTask.class, sourceSet, targetVariation, taskContainer );
		Task executeTask 	= TaskUtil.createTransformTask( "executeHxml", ExecuteHXMLTask.class, sourceSet, targetVariation, taskContainer );

		executeTask.dependsOn generateTask;

		return executeTask;
	}
}