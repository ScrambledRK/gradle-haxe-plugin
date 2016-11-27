package at.dotpoint.gradle.cross.transform.model.lifecycle;

import org.gradle.api.Task;

import java.util.List;

/**
 * Created by RK on 2016-11-27.
 */
public interface ILifeCycleTransformationDataInternal extends ILifeCycleTransformationData
{
	void setTasks( String name, List<Task> tasks );
}
