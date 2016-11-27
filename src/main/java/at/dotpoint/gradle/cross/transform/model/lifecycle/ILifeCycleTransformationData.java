package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.transform.model.ITaskTransformationData;
import org.gradle.api.Task;

import java.util.List;

/**
 * Created by RK on 2016-11-26.
 */
public interface ILifeCycleTransformationData extends ITaskTransformationData
{
	//
	List<Task> getTasks( String name );

	//
	<TTask extends Task> List<TTask> getTasks( String name, Class<TTask> type );
}
