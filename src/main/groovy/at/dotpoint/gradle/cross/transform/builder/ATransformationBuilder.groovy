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
abstract class ATransformationBuilder<TTarget,TInput>
{
	/**
	 *
	 */
	protected ArrayList<ITaskTransform<TTarget,TInput>> transformList;

	/**
	 *
	 */
	protected ArrayList<AssignedTransform<TTarget,TInput>> assignedTransforms;

	/**
	 *
	 */
	protected TaskContainer taskContainer;

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 *
	 * @param transformList
	 * @param taskContainer
	 */
	ATransformationBuilder( ArrayList<ITaskTransform<TTarget, TInput>> transformList, TaskContainer taskContainer )
	{
		this.assignedTransforms = new ArrayList<>();

		this.transformList = transformList;
		this.taskContainer = taskContainer;
	}

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 *
	 * @param binarySpec
	 */
	abstract public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec );

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	protected AssignedTransform<TTarget,TInput> getAssignedTransform( TTarget target,
	                                                                  TInput input )
	{
		for( AssignedTransform<TTarget,TInput> transform : this.assignedTransforms )
		{
			if( transform.target.equals( target ) && transform.input.equals( input ) )
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
			{
				AssignedTransform<TTarget,TInput> assignedTransform = new AssignedTransform<>( target,
						input, transform );

				// for what ever reason it might go wrong ...
				boolean success = this.assignedTransforms.add( assignedTransform );

				if( success )
					return assignedTransform;
			}

		}

		return null;
	}

	/**
	 *
	 * @param assigned
	 */
	protected void performTaskCreation( AssignedTransform<TTarget,TInput> assigned )
	{
		assigned.task = assigned.transform.createTransformTask( assigned.target, assigned.input,
				this.taskContainer );
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
