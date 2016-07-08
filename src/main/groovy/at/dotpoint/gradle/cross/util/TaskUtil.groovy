package at.dotpoint.gradle.cross.util

import org.gradle.api.Action
import org.gradle.api.DomainObjectSet
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.platform.base.BinarySpec
/**
 * Created by RK on 02.07.2016.
 */
class TaskUtil
{
	/**
	 *
	 * @param container
	 * @param name
	 * @return
	 */
	public static Task findTaskByName( DomainObjectSet<Task> container, String name )
	{
		Task current = null;

		container.each {
			if( it.name == name )
				current = it;
		}

		return current;
	}

	/**
	 *
	 * @param binarySpec
	 * @param sourceSet
	 * @param type
	 * @param name
	 * @return
	 */
	public static <TTask extends Task> TTask createBinaryTask( BinarySpec binarySpec, Class<TTask> type,
															   String name, Action<? super TTask> config )
	{
		TTask task = null;

		binarySpec.tasks.create( name, type )
		{
			task = it;
			config.execute( task );
		}

		// --------------- //

		return task;
	}

	/**
	 *
	 * @param prefix
	 * @param type
	 * @param sourceSet
	 * @param targetVariation
	 * @param taskContainer
	 * @return
	 */
	public static <TTask extends Task> TTask createTaskContainerTask( TaskContainer taskContainer, Class<TTask> type,
																		String name, Action<? super TTask> config )
	{
		TTask task = null;

		taskContainer.create( name, type )
		{
			task = it;
			config.execute( task );
		}

		// --------------- //

		return task;
	}

}
