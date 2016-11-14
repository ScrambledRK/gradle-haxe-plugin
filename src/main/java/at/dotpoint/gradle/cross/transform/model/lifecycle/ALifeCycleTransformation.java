package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.transform.model.ATaskTransformation;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

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
				this.performLifeCycle( binarySpec, testTask, CrossPlugin.NAME_TEST_SOURCE );
		}
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

	/**
	 */
	@Override
	public void updateTransformTask( IApplicationBinarySpec binarySpec )
	{
		// find ISourceTask tasks
		// apply dependencies and source sets

		// source sets from library dependencies (via binary spec)
		// artifacts from module dependencies (resolving directly)

		// also do it for the test components (not just binary)
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// general:

	/**
	 */
	protected void performLifeCycle( IApplicationBinarySpec binarySpec, Task task, String lifeCycleName )
	{
		Task lifeCycleTask = TaskUtil.findTaskByName( binarySpec, NameUtil.getBinaryTaskName( binarySpec, lifeCycleName ) );

		if( lifeCycleTask == null )
			throw new RuntimeException( "lifeCycleTask '" + lifeCycleName + "' not found for: " + binarySpec );

		lifeCycleTask.dependsOn( task );
	}

}
