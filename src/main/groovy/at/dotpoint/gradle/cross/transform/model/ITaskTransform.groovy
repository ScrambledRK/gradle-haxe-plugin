package at.dotpoint.gradle.cross.transform.model

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 08.07.2016.
 */
interface ITaskTransform<TTarget,TInput>
{
	/**
	 *
	 */
	boolean canTransform( TTarget target, TInput input );

	/**
	 *
	 */
	Task createTransformTask( TTarget target, TInput input, TaskContainer taskContainer );
}