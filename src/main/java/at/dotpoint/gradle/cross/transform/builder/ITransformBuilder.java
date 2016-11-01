package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
/**
 * Created by RK on 2016-08-27.
 */
public interface ITransformBuilder
{
	void createTransformationTasks( IApplicationBinarySpecInternal binarySpec );
}
