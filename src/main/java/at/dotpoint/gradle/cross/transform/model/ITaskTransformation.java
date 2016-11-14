package at.dotpoint.gradle.cross.transform.model;

import org.gradle.api.Task;
/**
 * Created by RK on 08.07.2016.
 */
public interface ITaskTransformation<TTarget>
{
	/**
	 *
	 */
	boolean canTransform( TTarget target );

	/**
	 *
	 */
	Task createTransformTask( TTarget target );

	/**
	 *
	 */
	void updateTransformTask( TTarget target );
}