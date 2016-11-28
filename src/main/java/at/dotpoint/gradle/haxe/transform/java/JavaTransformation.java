package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import at.dotpoint.gradle.haxe.task.java.ExecuteJarTask;
import at.dotpoint.gradle.haxe.task.java.HaxeJavaTask;
import at.dotpoint.gradle.haxe.transform.AHaxeTransformation;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by RK on 27.02.16.
 */
public class JavaTransformation extends AHaxeTransformation
{
	//
	public JavaTransformation( ServiceRegistry serviceRegistry )
	{
		super( serviceRegistry );
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	@Override
	protected boolean isValidTargetVariation( VariantCombination<IVariant> targetVariation )
	{
		return "java".equals( targetVariation.getVariant( IPlatform.class ).getName() );
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 * Convert
	 */
	protected List<Task> createConvertTransformation( IApplicationBinarySpec binarySpec, String name )
	{
		return Collections.singletonList( this.createHaxeTask( binarySpec, HaxeJavaTask.class, name ) );
	}

	/**
	 * Compile
	 */
	protected List<Task> createCompileTransformation( IApplicationBinarySpec binarySpec, String name )
	{
		return null;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	@Override
	protected Task createTestTask( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		String taskName = NameUtil.getBinaryTaskName( binarySpec, "test", testSpec.getName() );

		return TaskUtil.createTask( binarySpec, ExecuteJarTask.class, taskName, it ->
		{
			it.setJarFile( this.getBuildResultFile( binarySpec, testSpec.getName() ) );
			it.setMain( testSpec.getMain() );
		} );
	}

	//
	private File getBuildResultFile( IApplicationBinarySpec binarySpec, String name )
	{
		File libraryDir = new File( this.getOutputDirectory( binarySpec, name ), "build/libs" );
		String fileName = this.getBuildResultName( binarySpec );

		return new File( libraryDir, fileName );
	}

	//
	private String getBuildResultName( IApplicationBinarySpec binarySpec )
	{
		Project project = this.getProject( binarySpec );

		String version = project.getVersion().toString();
		String name = project.getName();


		return name + "-" + version + ".jar";
	}
}