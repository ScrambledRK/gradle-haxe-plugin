package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.task.ICrossTask
import at.dotpoint.gradle.cross.variant.iterator.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.DomainObjectSet
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.platform.base.BinarySpec

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
	 * @param binarySpec
	 * @param type
	 * @param name
	 * @return
	 */
	public static <TTask extends Task> TTask createBinaryTask( BinarySpec binarySpec, Class<TTask> type, String name )
	{
		TTask task = null;

		binarySpec.tasks.create( name, type )
		{
			task = it;
		}

		// --------------- //

		return task;
	}

	/**
	 *
	 * @param binarySpec
	 * @param sourceSet
	 * @param type
	 * @param name
	 * @return
	 */
	public static <TTask extends ICrossTask> TTask createBinaryTask( IApplicationBinarySpec binarySpec, ISourceSet sourceSet,
																	 Class<TTask> type, String name )
	{
		IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;
		IApplicationComponentSpecInternal applicationComponentSpec = (IApplicationComponentSpecInternal) binarySpec.application;

		ISourceSetInternal sourceSetInternal = (ISourceSetInternal)(sourceSet);

		// --------------- //

		TTask task = null;

		binarySpec.tasks.create( name, type )
		{
			task = it;

			it.inputPlatform 	= sourceSetInternal.sourcePlatform;
			it.outputPlatform 	= binarySpecInternal.targetPlatform;
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
	public static <TTask extends ICrossTask> TTask createTransformTask( String prefix, Class<TTask> type, ISourceSet sourceSet,
																		VariantCombination<IVariant> targetVariation,
																	    TaskContainer taskContainer )
	{
		String name = NameUtil.generateTransformTaskName( prefix, sourceSet, targetVariation );

		// --------------- //

		TTask task = null;

		taskContainer.create( name, type )
		{
			task = it;

			it.inputPlatform 	= sourceSet.sourcePlatform;
			it.outputPlatform 	= targetVariation.getVariant( IPlatform.class );
		}

		// --------------- //

		return task;
	}
}
