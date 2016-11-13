package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;

/**
 * Created by RK on 2016-08-27.
 */
public interface ILifeCycleTransformData
{
	IApplicationBinarySpecInternal getBinarySpec();
	void setBinarySpec( IApplicationBinarySpecInternal binarySpec );
}