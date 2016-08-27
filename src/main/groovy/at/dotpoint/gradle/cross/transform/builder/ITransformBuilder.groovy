package at.dotpoint.gradle.cross.transform.builder

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import org.gradle.api.tasks.TaskContainer

/**
 * Created by RK on 2016-08-27.
 */
interface ITransformBuilder
{
	void createTransformationTasks( IApplicationBinarySpecInternal binarySpec, TaskContainer taskContainer );
}
