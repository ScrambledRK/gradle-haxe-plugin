package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.transform.model.ATaskTransform;
import at.dotpoint.gradle.cross.util.TaskUtil;
import org.gradle.api.Task;
/**
 * Created by RK on 2016-08-27.
 */
public abstract class ALifeCycleTransform<TData extends ILifeCycleTransformData>
		extends ATaskTransform<IApplicationBinarySpec, TData>
		implements ILifeCycleTransform<TData>
{

	/**
	 *
	 * @return
	 */
	public abstract TData createTransformData();

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	public boolean canTransform( IApplicationBinarySpec target )
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
	public Task createTransformTask( IApplicationBinarySpec binarySpec,
	                          TData input )
	{
		Task convertTask = this.createConvertTransformation( binarySpec, input );
		Task compileTask = this.createCompileTransformation( binarySpec, input );
		Task testTask    = this.createTestTransformation( binarySpec, input );

		if( convertTask != null )
			this.performLifeCycle( binarySpec, convertTask, CrossPlugin.NAME_CONVERT_SOURCE );

		if( compileTask != null )
			this.performLifeCycle( binarySpec, compileTask, CrossPlugin.NAME_COMPILE_SOURCE );

		if( testTask != null )
			this.performLifeCycle( binarySpec, testTask,    CrossPlugin.NAME_TEST_SOURCE );

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
	                                                     TData input )

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	abstract protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                                     TData input )

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	abstract protected Task createTestTransformation( IApplicationBinarySpec binarySpec,
	                                                  TData input )

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

		if( lifeCycleTask == null )
			throw new RuntimeException( "lifeCycleTask '" + lifeCycleName + "' not found for: " + binarySpec );

		lifeCycleTask.dependsOn task;
	}
}
