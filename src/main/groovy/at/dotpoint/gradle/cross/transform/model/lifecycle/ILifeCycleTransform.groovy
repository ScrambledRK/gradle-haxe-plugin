package at.dotpoint.gradle.cross.transform.model.lifecycle

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.model.ITaskTransform

/**
 * Created by RK on 2016-08-27.
 */
interface ILifeCycleTransform extends ITaskTransform<IApplicationBinarySpec, ILifeCycleTransformData>
{
	/**
	 *
	 * @return
	 */
	ILifeCycleTransformData createTransformData();

	/**
	 *
	 * @param target
	 * @return
	 */
	boolean canTransform( IApplicationBinarySpec target );
}