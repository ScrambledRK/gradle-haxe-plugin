package at.dotpoint.gradle.cross.util;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.platform.base.BinarySpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by RK on 02.07.2016.
 */
public class TaskUtil
{
	/**
	 */
	public static <TTask extends Task> List<TTask> getTaskDependencies( Task target, Class<TTask> type )
	{
		List<TTask> result = new ArrayList<>();

		result.addAll( TaskUtil.getTaskDependencies( target ).stream()
				.filter( task -> type.isAssignableFrom( task.getClass() ) )
				.map( type::cast ).collect( Collectors.toList() )
		);

		return result;
	}

	/**
	 */
	public static <TTask extends Task> List<TTask> getTaskDependencies( Iterable<Task> container, Class<TTask> type )
	{
		List<TTask> result = new ArrayList<>();

		result.addAll( TaskUtil.getTaskDependencies( container ).stream()
				.filter( task -> type.isAssignableFrom( task.getClass() ) )
				.map( type::cast ).collect( Collectors.toList() )
		);

		return result;
	}

	/**
	 */
	public static List<Task> getTaskDependencies( Iterable<Task> container )
	{
		List<Task> dependencies = new ArrayList<>();

		for( Task task : container )
			dependencies.addAll( TaskUtil.getTaskDependencies( task ) );

		return dependencies;
	}

	/**
	 */
	public static List<Task> getTaskDependencies( Task task )
	{
		List<Task> dependencies = new ArrayList<>();

		// ---------- //

		Set<? extends Task> taskSet = task.getTaskDependencies().getDependencies( task );

		for( Task dependency : taskSet )
		{
			dependencies.add( dependency );
			dependencies.addAll( TaskUtil.getTaskDependencies( dependency ) );
		}

		// ---------- //

		return dependencies;
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// find by name:

	/**
	 */
	public static Task findTaskByName( BinarySpec binarySpec, String name )
	{
		return TaskUtil.findTaskByName( binarySpec.getTasks(), name );
	}

	/**
	 */
	public static <TTask extends Task> TTask findTaskByName( BinarySpec binarySpec,
	                                                         String name, Class<TTask> type )
	{
		return TaskUtil.findTaskByName( binarySpec.getTasks(), name, type );
	}

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

	// ***************************************************************** //
	// ***************************************************************** //
	// create:

	/**
	 */
	public static <TTask extends Task> TTask createTask( BinarySpec binarySpec, Class<TTask> type,
	                                                     String name, Action<? super TTask> config )
	{
		if( TaskUtil.findTaskByName( binarySpec, name ) != null )
			throw new RuntimeException( "cannot create task, because a task with name: " + name + " already exists" );

		if( config == null )
		{
			config = tTask -> {
				//;
			};
		}

		binarySpec.getTasks().create( name, type, config::execute );
		return TaskUtil.findTaskByName( binarySpec, name, type );
	}

}
