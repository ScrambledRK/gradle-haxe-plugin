package at.dotpoint.gradle.cross.transform.model;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;

/**
 * Created by RK on 08.07.2016.
 */
public interface ITaskTransformation<TTarget extends ITaskTransformationData>
{
	/**
	 *
	 */
	boolean canTransform( IApplicationBinarySpec target );

	/**
	 *
	 */
	void createTransformTask( TTarget target );

	/**
	 *
	 */
	void updateTransformTask( TTarget target );
}