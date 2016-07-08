package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.compile.ACompileTransform
import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 27.02.16.
 */
class HaxeCompileTransform extends ACompileTransform
{

	/**
	 * SourceSet to convert
	 */
	@Override
	protected boolean isValidTransformTarget( IApplicationBinarySpec binarySpec )
	{
		return true
	}

	/**
	 * target VariantCombination to transform the target SourceSet to
     */
	@Override
	protected boolean isValidTransformInput( List<ISourceSet> sources )
	{
		return true;
	}

	@Override
	protected boolean isValidVariant( IVariant variant )
	{
		return variant instanceof IPlatform;
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
	Task createTransformTask( IApplicationBinarySpec binarySpec, List<ISourceSet> sources, TaskContainer taskContainer )
	{
		//
		Task generateTask = TaskUtil.createBinaryTask( binarySpec, GenerateHXMLTask.class,
				NameUtil.generateTransformTaskName( "generateCompileHxml", binarySpec.targetVariantCombination ) )
		{

		};

		//
		Task executeTask = TaskUtil.createBinaryTask( binarySpec, ExecuteHXMLTask.class,
				NameUtil.generateTransformTaskName( "executeCompileHxml", binarySpec.targetVariantCombination ) )
		{

		};

		executeTask.dependsOn generateTask;

		// -------- //

		return executeTask;
	}
}