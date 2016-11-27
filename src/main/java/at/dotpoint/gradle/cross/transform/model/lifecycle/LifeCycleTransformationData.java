package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.transform.model.TaskTransformationData;
import org.gradle.api.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by RK on 2016-11-26.
 */
public class LifeCycleTransformationData extends TaskTransformationData implements ILifeCycleTransformationDataInternal
{
	//
	private HashMap<String,List<Task>> taskListMap;

	//
	public LifeCycleTransformationData( IApplicationBinarySpec binarySpec )
	{
		super( binarySpec );

		// small table
		this.taskListMap = new HashMap<>( 8, 0.75f );
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	@Override
	public void setTasks( String name, List<Task> tasks )
	{
		if( this.taskListMap.containsKey( name ) )
			throw new IllegalArgumentException( "key " + name + " already set in LifeCycleTransformationData" );

		this.taskListMap.put( name, tasks );
	}

	@Override
	public List<Task> getTasks( String name )
	{
		return Collections.unmodifiableList( this.taskListMap.get( name ) );
	}

	@Override
	public <TTask extends Task> List<TTask> getTasks( String name, Class<TTask> type )
	{
		List<TTask> result = new ArrayList<>();

		result.addAll( this.getTasks( name ).stream()
				.filter( task -> type.isAssignableFrom( task.getClass() ) )
				.map( type::cast ).collect( Collectors.toList() )
		);

		return result;
	}
}
