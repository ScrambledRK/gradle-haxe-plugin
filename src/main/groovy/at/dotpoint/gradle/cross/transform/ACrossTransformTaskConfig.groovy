package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.task.ICrossTask
import at.dotpoint.gradle.cross.task.TransformTask
import at.dotpoint.gradle.cross.util.TaskUtil
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.LanguageSourceSet
import org.gradle.platform.base.BinarySpec

/**
*  Created by RK on 02.07.2016.
*/
abstract class ACrossTransformTaskConfig implements ICrossTransformTaskConfig
{

	@Override
	String getTaskPrefix()
	{
		return "transform"
	}

	@Override
	Class<? extends ICrossTask> getTaskType()
	{
		return TransformTask.class;
	}

	// ----------------------------- //
	// ----------------------------- //

	//
	void configureTask( Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry )
	{
		if( !(task instanceof ICrossTask) )
			throw new Exception("cannot configure CrossTransformTask, given Task not supported");

		if( !(binary instanceof IApplicationBinarySpec) )
			throw new Exception("cannot configure CrossTransformTask, given BinarySpec not supported");

		if( !(sourceSet instanceof ISourceSet) )
			throw new Exception("cannot configure CrossTransformTask, given LanguageSourceSet not supported");

		this.configureTask( (ICrossTask) task, (IApplicationBinarySpec) binary, (ISourceSet) sourceSet, serviceRegistry );
	}

	//
	void configureAdditionalTransform( Task task, LanguageSourceSet sourceSet )
	{
		if( !(task instanceof ICrossTask) )
			throw new Exception("cannot configure CrossTransformTask, given Task not supported");

		if( !(sourceSet instanceof ISourceSet) )
			throw new Exception("cannot configure CrossTransformTask, given LanguageSourceSet not supported");

		this.configureAdditionalTransform( (ICrossTask) task, (ISourceSet) sourceSet );
	}

	//
	boolean canTransform( LanguageSourceSet candidate )
	{
		if( candidate instanceof ISourceSet )
			return this.canTransform( (ISourceSet) candidate );

		return false;
	}

	@Override
	void configureAdditionalTransform( ICrossTask task, ISourceSet sourceSet)
	{
		this.configureAdditionalTransform( (TransformTask) task, sourceSet );
	}

	// ----------------------------- //
	// ----------------------------- //

	/**
	 *
	 * @param task				actually not used ...
	 * @param binary
	 * @param sourceSet
	 * @param serviceRegistry
	 */
	void configureTask( ICrossTask task, IApplicationBinarySpec binarySpec, ISourceSet sourceSet, ServiceRegistry serviceRegistry )
	{
		TransformTask transformTask = (TransformTask) task;		// abusing this task to pass data; will be destroyed

		transformTask.binarySpec 		= binarySpec;
		transformTask.serviceRegistry 	= serviceRegistry;

		// ----------------- //

		if( !this.canTransform( sourceSet ) )
			throw new Exception( "the amazing LanguageTransform system accidentally accepted a SourceSet impossible to transform" );

		this.configureCycles( binarySpec, sourceSet, serviceRegistry, false );
	}

	/**
	 *
	 * @param task
	 * @param sourceSet
	 */
	void configureAdditionalTransform( TransformTask task, ISourceSet sourceSet )
	{
		this.configureCycles( task.binarySpec, sourceSet, task.serviceRegistry, true );
	}

	/**
	 *
	 * @param cycle
	 * @param isJoint
	 */
	void configureCycles( IApplicationBinarySpec binarySpec, ISourceSet sourceSet, ServiceRegistry serviceRegistry, boolean isJoint )
	{
		List<String> cycles = [ CrossPlugin.NAME_CONVERT_SOURCE, CrossPlugin.NAME_COMPILE_SOURCE ];

		cycles.each { String cycle ->

			Task parentTask = TaskUtil.findTaskByName( binarySpec.tasks, binarySpec.tasks.taskName(cycle) );
			Task cycleTask  = this.createSourceSetCycleTask( parentTask, cycle, binarySpec, sourceSet, serviceRegistry, isJoint );

			if( cycleTask != null )
				parentTask.dependsOn cycleTask;
		}
	}

	// ----------------------------- //
	// ----------------------------- //

	/**
	 * actually create the sourceSet transformation tasks
	 *
	 * @param parent			LifeCycleTask of the BinarySpec; convert or compile; dependsOn will be set for newly created task
	 * @param cycle				LifeCycle name; convert or compile
	 * @param binarySpec		variation permutation to transform (convert, compile)
	 * @param sourceSet			one of possible many source sets
	 * @param serviceRegistry	in case you need some fancy new classes
	 * @param isJoint			false - first run for the first sourceSet, true - followup runs for other sourceSets
	 * @return					sourceSet specific convert or compile task
	 */
	abstract Task createSourceSetCycleTask( Task parent, String cycle, IApplicationBinarySpec binarySpec, ISourceSet sourceSet, ServiceRegistry serviceRegistry, boolean isJoint  );

	/**
	 * in case multiple sourceSets are attached to a binarySpec you'll be asked if you can transform the other ones as well
	 *
	 * @param candidate
	 * @return
	 */
	abstract boolean canTransform( ISourceSet candidate );

}

