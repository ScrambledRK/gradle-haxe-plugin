package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.transform.model.ITaskTransformation;
import at.dotpoint.gradle.cross.transform.model.ITaskTransformationData;
import org.gradle.api.Task;

/**
 */
public class AssignedTransform<TTarget extends ITaskTransformationData>
{
	//
	public TTarget target;

	//
	public ITaskTransformation<TTarget> transform;

	//
	public Task task;

	/**
	 */
	AssignedTransform( TTarget target, ITaskTransformation<TTarget> transform )
	{
		this.target = target;
		this.transform = transform;
	}
}
