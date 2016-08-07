package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.transform.convert.AConvertTransform
import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 27.02.16.
 */
class HaxeConvertTransform extends AConvertTransform
{

	/**
	 * SourceSet to convert
	 */
	@Override
	protected boolean isValidTransformTarget( ISourceSet iSourceSet )
	{
		return true
	}

	/**
	 * target VariantCombination to transform the target SourceSet to
     */
	@Override
	protected boolean isValidTransformInput( VariantCombination<IVariant> target )
	{
		return true;
	}

	@Override
	protected boolean isValidVariant( IVariant variant )
	{
		return variant instanceof IPlatform || variant instanceof IFlavor;
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
		//
		Task generateTask = TaskUtil.createTaskContainerTask( taskContainer, GenerateHXMLTask.class,
				NameUtil.generateTransformTaskName( "generateConvertHxml", sourceSet, targetVariation ) )
		{
			it.sourceVariantCombination.platform = sourceSet.sourcePlatform;
			it.targetVariantCombination.platform = targetVariation.platform;
			it.targetVariantCombination.flavor 	 = targetVariation.flavor;

			it.sourceSet = sourceSet;
		};

		//
		Task executeTask = TaskUtil.createTaskContainerTask( taskContainer, ExecuteHXMLTask.class,
				NameUtil.generateTransformTaskName( "executeConvertHxml", sourceSet, targetVariation ) )
		{
			it.hxmlFile = (generateTask as GenerateHXMLTask).hxmlFile;
		};

		executeTask.dependsOn generateTask;

		// -------- //

		return executeTask;
	}
}