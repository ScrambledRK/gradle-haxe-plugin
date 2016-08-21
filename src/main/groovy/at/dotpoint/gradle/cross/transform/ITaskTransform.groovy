package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
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
	VariantCombination<IVariant> canTransform( TTarget target, TInput input );

	/**
	 *
	 */
	Task createTransformTask( TTarget target, TInput input, TaskContainer taskContainer );
}