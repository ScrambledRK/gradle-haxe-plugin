package at.dotpoint.gradle.cross.transform.model;

import org.gradle.api.Task;
/**
 * Created by RK on 08.07.2016.
 */
public interface ITaskTransform<TTarget,TInput>
{
	/**
	 *
	 */
	boolean canTransform( TTarget target, TInput input );

	/**
	 *
	 */
	Task createTransformTask( TTarget target, TInput input );
}