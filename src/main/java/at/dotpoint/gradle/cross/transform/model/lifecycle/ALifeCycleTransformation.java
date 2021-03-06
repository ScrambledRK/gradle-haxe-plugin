package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.task.ISourceTask;
import at.dotpoint.gradle.cross.transform.model.ATaskTransformation;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 2016-08-27.
 */
public abstract class ALifeCycleTransformation extends ATaskTransformation<ILifeCycleTransformationDataInternal>
		implements ILifeCycleTransformation
{
	//
	private static final Logger LOGGER = Logging.getLogger(ILifeCycleTransformation.class);
	
	//
	public ALifeCycleTransformation( ServiceRegistry serviceRegistry )
	{
		super( serviceRegistry );
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// create:


	/**
	 */
	@Override
	final public void createTransformTask( ILifeCycleTransformationDataInternal target )
	{
		this.createTransformTask( target, CrossPlugin.NAME_CONVERT_SOURCE );
		this.createTransformTask( target, CrossPlugin.NAME_COMPILE_SOURCE );

		for( ITestComponentSpec testSpec : target.getBinarySpec().getApplication().getTests() )
			this.createTransformTask( target, testSpec );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	private void createTransformTask( ILifeCycleTransformationDataInternal target, String lifeCycleName )
	{
		List<Task> tasks;

		switch( lifeCycleName )
		{
			//
			case CrossPlugin.NAME_CONVERT_SOURCE:
				tasks = this.createConvertTransformation( target );
				break;

			//
			case CrossPlugin.NAME_COMPILE_SOURCE:
				tasks = this.createCompileTransformation( target );
				break;

			default:
				throw new RuntimeException( "unexpected lifeCycle: " + lifeCycleName );
		}

		// --------------- //

		target.setTasks( lifeCycleName, tasks );

		IApplicationBinarySpec binarySpec = target.getBinarySpec();
		String binaryTaskName = NameUtil.getBinaryTaskName( binarySpec, lifeCycleName );

		this.performLifeCycle( binarySpec, tasks, binaryTaskName );
	}

	/**
	 */
	private void createTransformTask( ILifeCycleTransformationDataInternal target, ITestComponentSpec testSpec )
	{
		List<Task> tasks = this.createTestTransformation( target, testSpec );
		target.setTasks( testSpec.getName(), tasks );

		// --------------- //

		IApplicationBinarySpec binarySpec = target.getBinarySpec();
		String binaryTaskName = NameUtil.getBinaryTaskName( binarySpec, CrossPlugin.NAME_TEST_SOURCE );

		this.performLifeCycle( binarySpec, tasks, binaryTaskName );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	//
	// TODO: lifecycle task transformation task list order for lifecycle dependency sucks ...
	//
	private void performLifeCycle( IApplicationBinarySpec binarySpec, List<Task> tasks, String binaryTaskName )
	{
		if( tasks != null && !tasks.isEmpty() )
		{
			Task task = tasks.get( tasks.size() - 1 );

			if( task == null )
				throw new RuntimeException( "task  '" + binaryTaskName + "' not found for: " + binarySpec );

			this.performLifeCycle( binarySpec, task, binaryTaskName );
		}
	}

	/**
	 */
	private void performLifeCycle( IApplicationBinarySpec binarySpec, Task task, String binaryTaskName )
	{
		Task lifeCycleTask = TaskUtil.findTaskByName( binarySpec, binaryTaskName );

		if( lifeCycleTask == null )
			throw new RuntimeException( "lifeCycleTask '" + binaryTaskName + "' not found for: " + binarySpec );

		lifeCycleTask.dependsOn( task );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	abstract protected List<Task> createConvertTransformation( ILifeCycleTransformationData target );

	/**
	 */
	abstract protected List<Task> createCompileTransformation( ILifeCycleTransformationData target );

	/**
	 */
	abstract protected List<Task> createTestTransformation( ILifeCycleTransformationData target,
	                                                        ITestComponentSpec testSpec );

	// ***************************************************************** //
	// ***************************************************************** //
	// update:

	/**
	 */
	@Override
	final public void updateTransformTask( ILifeCycleTransformationDataInternal target )
	{
		this.updateTransformTask( target, CrossPlugin.NAME_CONVERT_SOURCE );
		this.updateTransformTask( target, CrossPlugin.NAME_COMPILE_SOURCE );

		for( ITestComponentSpec testComponentSpec : target.getBinarySpec().getApplication().getTests() )
			this.updateTransformTask( target, testComponentSpec );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	protected void updateTransformTask( ILifeCycleTransformationData target, String lifeCycleName )
	{
		IApplicationBinarySpec binarySpec = target.getBinarySpec();
		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		List<ISourceTask> tasks = target.getTasks( lifeCycleName, ISourceTask.class );

		this.updateSourceTasks( binarySpec, sourceSets, tasks );
	}

	/**
	 */
	protected void updateTransformTask( ILifeCycleTransformationData target,
	                                    ITestComponentSpec testSpec )
	{
		IApplicationBinarySpec binarySpec = target.getBinarySpec();

		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		sourceSets.addAll( BinarySpecUtil.getSourceSetList( testSpec ) );

		List<ISourceTask> tasks = target.getTasks( testSpec.getName(), ISourceTask.class );

		this.updateSourceTasks( binarySpec, sourceSets, tasks );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	private void updateSourceTasks( IApplicationBinarySpec binarySpec, List<ISourceSet> sourceSetList,
	                                List<ISourceTask> tasks )
	{
		List<IApplicationBinarySpec> libraries = this.getLibraryDependencies(
				sourceSetList, binarySpec.getTargetVariantCombination() );

		Set<File> artifacts = this.getModuleArtifacts( binarySpec, sourceSetList );

		// ------------- //

		for( ISourceTask task : tasks )
		{
			LOGGER.info( "-task: {}", task );
			LOGGER.info( "-sourceSetList: {}", sourceSetList );
			LOGGER.info( "-artifacts: {}", artifacts );
			LOGGER.info( "-libraries: {}", libraries );

			task.source( sourceSetList );
			task.dependency( artifacts );

			if( task.getSourceDirectories().isEmpty() )
				LOGGER.warn( "source directory list is empty, task {} will not execute", task );
			
			for( IApplicationBinarySpec library : libraries )
			{
				task.source( BinarySpecUtil.getSourceSetList( library ) );
				task.dependsOn( library.getBuildTask() );
			}
		}
	}

}
