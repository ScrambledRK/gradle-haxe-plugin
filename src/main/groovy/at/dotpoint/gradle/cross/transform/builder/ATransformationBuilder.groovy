package at.dotpoint.gradle.cross.transform.builder

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.transform.model.ITaskTransform
import at.dotpoint.gradle.cross.util.TaskUtil
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

/**
 *
 * @param <TTarget>
 * @param <TInput>
 */
abstract class ATransformationBuilder<TTarget,TInput> implements ITransformBuilder
{
	/**
	 *
	 */
	protected ArrayList<ITaskTransform<TTarget,TInput>> transformList;

	/**
	 *
	 */
	protected ArrayList<AssignedTransform<TTarget,TInput>> assignedTransforms;

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 *
	 * @param transformList
	 * @param taskContainer
	 */
	ATransformationBuilder( ArrayList<ITaskTransform<TTarget, TInput>> transformList )
	{
		this.assignedTransforms = new ArrayList<>();
		this.transformList = transformList;
	}

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 *
	 * @param binarySpec
	 */
	abstract public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec,
	                                                TaskContainer taskContainer );

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	protected AssignedTransform<TTarget,TInput> getAssignedTransform( TTarget target,
	                                                                  TInput input )
	{
		AssignedTransform<TTarget,TInput> assignedTransform = this.getAssignedTransform( target );

		if( assignedTransform != null && assignedTransform.input.equals( input ) )
			return assignedTransform;

		return null;
	}

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	protected AssignedTransform<TTarget,TInput> getAssignedTransform( TTarget target )
	{
		for( AssignedTransform<TTarget,TInput> transform : this.assignedTransforms )
		{
			if( transform.target.equals( target )  )
				return transform;
		}

		return null;
	}

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	protected AssignedTransform<TTarget,TInput> assignTransformation( TTarget target,
	                                                                  TInput input )
	{
		for( ITaskTransform<TTarget,TInput> transform : this.transformList )
		{
			if( transform.canTransform( target, input ) )
				return this.createAssignedTransform( target, input, transform );
		}

		return null;
	}

	/**
	 *
	 * @param target
	 * @param input
	 * @param transform
	 * @return
	 */
	protected AssignedTransform<TTarget,TInput> createAssignedTransform( TTarget target,
	                                                                     TInput input,
	                                                                     ITaskTransform<TTarget,TInput> transform )
	{
		AssignedTransform<TTarget,TInput> assignedTransform = new AssignedTransform<>( target, input, transform );

		// for what ever reason it might go wrong ...
		boolean success = this.assignedTransforms.add( assignedTransform );

		if( success )
			return assignedTransform;

		return null;
	}

	/**
	 *
	 * @param assigned
	 */
	protected void performTaskCreation( AssignedTransform<TTarget,TInput> assigned,
	                                    TaskContainer taskContainer )
	{
		assigned.task = assigned.transform.createTransformTask( assigned.target, assigned.input, taskContainer );
	}

	/**
	 *
	 * @param assignedTransform
	 * @param binarySpec
	 */
	protected void performLifeCycle( AssignedTransform assignedTransform,
	                                 IApplicationBinarySpecInternal binarySpec,
	                                 String taskName )
	{
		Task convertTask = TaskUtil.findTaskByName( binarySpec.tasks,
				binarySpec.tasks.taskName( taskName ) );

		convertTask.dependsOn assignedTransform.task;
	}
}

/**
 *
 * @param <TTarget>
 * @param <TInput>
 */
class AssignedTransform<TTarget,TInput>
{
	//
	public TTarget target;

	//
	public TInput input;

	//
	public ITaskTransform<TTarget,TInput> transform;

	//
	public Task task;

	/**
	 *
	 * @param target
	 * @param input
	 * @param transform
	 */
	AssignedTransform( TTarget target, TInput input, ITaskTransform<TTarget,TInput> transform )
	{
		this.target = target
		this.input = input
		this.transform = transform;
	}
}
