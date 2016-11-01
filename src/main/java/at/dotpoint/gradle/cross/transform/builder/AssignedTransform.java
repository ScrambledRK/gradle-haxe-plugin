package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.transform.model.ITaskTransform;
import org.gradle.api.Task;

/**
 *
 * @param <TTarget>
 * @param <TInput>
 */
public class AssignedTransform<TTarget,TInput>
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
	 */
	AssignedTransform( TTarget target, TInput input, ITaskTransform<TTarget,TInput> transform )
	{
		this.target = target;
		this.input = input;
		this.transform = transform;
	}
}
