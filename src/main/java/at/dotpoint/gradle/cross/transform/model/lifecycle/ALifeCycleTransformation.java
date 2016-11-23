package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.task.ISourceTask;
import at.dotpoint.gradle.cross.transform.model.ATaskTransformation;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 2016-08-27.
 */
public abstract class ALifeCycleTransformation extends ATaskTransformation<IApplicationBinarySpec>
		implements ILifeCycleTransformation
{
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
	public Task createTransformTask( IApplicationBinarySpec binarySpec )
	{
		Task convertTask = this.createConvertTransformation( binarySpec );
		Task compileTask = this.createCompileTransformation( binarySpec );

		if( convertTask != null )
			this.performLifeCycle( binarySpec, convertTask, CrossPlugin.NAME_CONVERT_SOURCE );

		if( compileTask != null )
			this.performLifeCycle( binarySpec, compileTask, CrossPlugin.NAME_COMPILE_SOURCE );

		this.createTestComponentTasks( binarySpec );

		// -------- //

		return null;
	}

	/**
	 */
	private void createTestComponentTasks( IApplicationBinarySpec binarySpec )
	{
		IApplicationComponentSpec componentSpec = binarySpec.getApplication();

		for( ITestComponentSpec testComponentSpec : componentSpec.getTests() )
		{
			Task testTask = this.createTestTransformation( binarySpec, testComponentSpec );

			if( testTask != null )
			{
				Task lifeCycleTask = TaskUtil.createTask( binarySpec, DefaultTask.class,
						this.generateTestTaskName( binarySpec, testComponentSpec ), null );

				lifeCycleTask.dependsOn( testTask );

				this.performLifeCycle( binarySpec, lifeCycleTask, CrossPlugin.NAME_TEST_SOURCE );
			}

		}
	}

	private String generateTestTaskName( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		return NameUtil.getBinaryTaskName( binarySpec, "test", testSpec.getName() );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	abstract protected Task createConvertTransformation( IApplicationBinarySpec binarySpec );

	/**
	 */
	abstract protected Task createCompileTransformation( IApplicationBinarySpec binarySpec );

	/**
	 */
	abstract protected Task createTestTransformation( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec );

	// ***************************************************************** //
	// ***************************************************************** //
	// update:

	// find ISourceTask tasks
	// apply dependencies and source sets

	// source sets from library dependencies (via binary spec)
	// artifacts from module dependencies (resolving directly)

	// also do it for the test components (not just binary)

	/**
	 */
	@Override
	public void updateTransformTask( IApplicationBinarySpec binarySpec )
	{
		this.updateConvertTransformation( binarySpec );
	//	this.updateCompileTransformation( binarySpec ); // BUG: will update same tasks twice, cannot use task dep!
		this.updateTestComponentTasks( binarySpec );
	}

	/**
	 */
	private void updateTestComponentTasks( IApplicationBinarySpec binarySpec )
	{
		IApplicationComponentSpec componentSpec = binarySpec.getApplication();

		for( ITestComponentSpec testComponentSpec : componentSpec.getTests() )
			this.updateTestTransformation( binarySpec, testComponentSpec );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	private void updateConvertTransformation( IApplicationBinarySpec binarySpec )
	{
		Task lifeCycleTask = this.getLifeCycleTask( binarySpec, CrossPlugin.NAME_CONVERT_SOURCE );

		if( lifeCycleTask == null )
			return;

		this.updateSourceTasks( binarySpec,
				BinarySpecUtil.getSourceSetList( binarySpec ),
				TaskUtil.getTaskDependencies( lifeCycleTask, ISourceTask.class ) );
	}

	/**
	 */
	private void updateCompileTransformation( IApplicationBinarySpec binarySpec )
	{
		Task lifeCycleTask = this.getLifeCycleTask( binarySpec, CrossPlugin.NAME_COMPILE_SOURCE );

		if( lifeCycleTask == null )
			return;

		this.updateSourceTasks( binarySpec,
				BinarySpecUtil.getSourceSetList( binarySpec ),
				TaskUtil.getTaskDependencies( lifeCycleTask, ISourceTask.class ) );
	}

	/**
	 */
	private void updateTestTransformation( IApplicationBinarySpec binarySpec, ITestComponentSpec testComponentSpec )
	{
		String taskName = this.generateTestTaskName( binarySpec, testComponentSpec );
		Task lifeCycleTask = TaskUtil.findTaskByName( binarySpec, taskName );

		if( lifeCycleTask == null )
			return;

		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		sourceSets.addAll( BinarySpecUtil.getSourceSetList( testComponentSpec ) );

		this.updateSourceTasks( binarySpec, sourceSets,
				TaskUtil.getTaskDependencies( lifeCycleTask, ISourceTask.class ) );
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
			System.out.println("----");
			System.out.println( task );
			System.out.println( sourceSetList );
			System.out.println( artifacts );
			System.out.println( libraries );

			task.source( sourceSetList );
			task.dependency( artifacts );

			for( IApplicationBinarySpec library : libraries )
			{
				task.source( BinarySpecUtil.getSourceSetList( library ) );
				task.dependsOn( library.getBuildTask() );
			}
		}
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// general:

	/**
	 */
	private void performLifeCycle( IApplicationBinarySpec binarySpec, Task task, String lifeCycleName )
	{
		Task lifeCycleTask = this.getLifeCycleTask( binarySpec, lifeCycleName );

		if( lifeCycleTask == null )
			throw new RuntimeException( "lifeCycleTask '" + lifeCycleName + "' not found for: " + binarySpec );

		lifeCycleTask.dependsOn( task );
	}

	/**
	 */
	private Task getLifeCycleTask( IApplicationBinarySpec binarySpec, String lifeCycleName )
	{
		return TaskUtil.findTaskByName( binarySpec, NameUtil.getBinaryTaskName( binarySpec, lifeCycleName ) );
	}

}
