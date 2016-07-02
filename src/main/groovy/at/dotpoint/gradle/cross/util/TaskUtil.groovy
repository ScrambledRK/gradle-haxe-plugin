package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.task.APlatformTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Task
/**
 * Created by RK on 02.07.2016.
 */
class TaskUtil
{
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
	 */
	public static <TTask extends APlatformTask> TTask createBinaryTask( IApplicationBinarySpec binarySpec, Class<TTask> type, String name )
	{
		IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;
		IApplicationComponentSpecInternal applicationComponentSpec = (IApplicationComponentSpecInternal) binarySpec.application;

		ISourceSetInternal sourceSet = (ISourceSetInternal)(applicationComponentSpec.sources.get( CrossPlugin.NAME_COMPILE_SOURCE ));

		// --------------- //

		TTask task = null;

		binarySpec.tasks.create( name, type )
		{
			task = it;

			it.inputPlatform 	= sourceSet.sourcePlatform;
			it.outputPlatform 	= binarySpecInternal.targetPlatform;
		}

		// --------------- //

		return task;
	}
}
