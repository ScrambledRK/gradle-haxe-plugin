package at.dotpoint.gradle.cross.util;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.platform.base.BinarySpec;
/**
 * Created by RK on 02.07.2016.
 */
public class TaskUtil
{
	/**
	 */
	public static <TTask extends Task> TTask findTaskByName( Iterable<Task> container,
	                                                         String name, Class<TTask> type )
	{
		Task task = TaskUtil.findTaskByName( container, name );

		if( task != null && type.isAssignableFrom( task.getClass() ) )
			return type.cast( task );

		return null;
	}

	/**
	 */
	public static Task findTaskByName( Iterable<Task> container, String name )
	{
		for( Task task : container )
		{
			if( name.equals( task.getName() ) )
				return task;
		}

		return null;
	}

	/**
	 */
	public static <TTask extends Task> TTask createTask( BinarySpec binarySpec, Class<TTask> type,
	                                                     String name, Action<? super TTask> config )
	{
		binarySpec.getTasks().create( name, type, config::execute );
		return TaskUtil.findTaskByName( binarySpec.getTasks(), name, type );
	}

	/**
	 */
	public static <TTask extends Task> TTask createTask( TaskContainer taskContainer, Class<TTask> type,
	                                                     String name, Action<? super TTask> config )
	{
		taskContainer.create( name, type, config::execute );
		return TaskUtil.findTaskByName( taskContainer, name, type );
	}

}
