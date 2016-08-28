package at.dotpoint.gradle.cross.transform.model.lifecycle

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.transform.model.ATaskTransform
import at.dotpoint.gradle.cross.util.TaskUtil
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 2016-08-27.
 */
abstract class ALifeCycleTransform extends ATaskTransform<IApplicationBinarySpec, ILifeCycleTransformData>
		implements ILifeCycleTransform
{
	/**
	 *
	 * @return
	 */
	abstract ILifeCycleTransformData createTransformData();

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	boolean canTransform( IApplicationBinarySpec target )
	{
		return this.isValidTransformTarget( target );
	}

	/**
	 *
	 * @param target
	 * @return
	 */
	protected boolean isValidTransformInput( ILifeCycleTransformData input )
	{
		return true;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	@Override
	Task createTransformTask( IApplicationBinarySpec binarySpec,
	                          ILifeCycleTransformData input,
	                          TaskContainer taskContainer )
	{
		if( binarySpec.isTestBinarySpec() ) // if test, lifecycle of binary tested
		{
			IApplicationBinarySpecInternal testBinary = (IApplicationBinarySpecInternal) binarySpec;
			IApplicationBinarySpecInternal compBinary = (IApplicationBinarySpecInternal) testBinary.testBinarySpecSource;

			// -------- //

			Task testTask = this.createTestTransformation( binarySpec, input, taskContainer );

			if( testTask != null )
				this.performLifeCycle( compBinary, testTask, CrossPlugin.NAME_TEST_SOURCE );
		}

		// -------- //

		Task convertTask = this.createConvertTransformation( binarySpec, input, taskContainer );
		Task compileTask = this.createCompileTransformation( binarySpec, input, taskContainer );

		if( convertTask != null )
			this.performLifeCycle( binarySpec, convertTask, CrossPlugin.NAME_CONVERT_SOURCE );

		if( compileTask != null )
			this.performLifeCycle( binarySpec, compileTask, CrossPlugin.NAME_COMPILE_SOURCE );

		// -------- //

		return null;
	}
	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	abstract protected Task createConvertTransformation( IApplicationBinarySpec binarySpec,
	                                          ILifeCycleTransformData input,
	                                          TaskContainer taskContainer )

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	abstract protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                          ILifeCycleTransformData input,
	                                          TaskContainer taskContainer )

	/**
	 *
	 * @param testBinary binary testing the sourceBinary
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	abstract protected Task createTestTransformation( IApplicationBinarySpec testBinary,
	                                                  ILifeCycleTransformData input,
	                                                  TaskContainer taskContainer )

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 * @param task
	 * @param lifeCycleName
	 */
	protected void performLifeCycle( IApplicationBinarySpec binarySpec, Task task, String lifeCycleName )
	{
		Task lifeCycleTask = TaskUtil.findTaskByName( binarySpec.tasks,
				binarySpec.tasks.taskName( lifeCycleName ) );

		lifeCycleTask.dependsOn task;
	}
}
