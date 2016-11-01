package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.transform.model.ITaskTransform;

/**
 * Created by RK on 2016-08-27.
 */
public interface ILifeCycleTransform<TData extends ILifeCycleTransformData>
		extends ITaskTransform<IApplicationBinarySpec, TData>
{
	/**
	 *
	 * @return
	 */
	TData createTransformData();

	/**
	 *
	 * @param target
	 * @return
	 */
	boolean canTransform( IApplicationBinarySpec target );
}