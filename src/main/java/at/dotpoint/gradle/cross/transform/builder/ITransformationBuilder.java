package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
/**
 * Created by RK on 2016-08-27.
 */
public interface ITransformationBuilder
{
	void createTransformationTasks( IApplicationBinarySpecInternal binarySpec );
	void updateTransformationTasks( IApplicationBinarySpecInternal binarySpec );
}
