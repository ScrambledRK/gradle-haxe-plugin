package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransformation;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask;
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask;
import at.dotpoint.gradle.haxe.task.java.ExecuteGradleTask;
import at.dotpoint.gradle.haxe.task.java.ExecuteJarTask;
import at.dotpoint.gradle.haxe.task.java.GenerateGradleTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.language.base.LanguageSourceSet;

import java.io.File;
import java.util.Iterator;

/**
 * Created by RK on 27.02.16.
 */
public class JavaTransformation extends ALifeCycleTransformation
{
	//
	public JavaTransformation( ServiceRegistry serviceRegistry )
	{
		super( serviceRegistry );
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 * SourceSet to convert
	 */
	@Override
	public boolean canTransform( IApplicationBinarySpec binarySpec )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.getTargetVariantCombination();

		if( targetVariation.getVariant( IPlatform.class ).getName() != "java" )
			return false;

		if( !this.isValidSourceSets( binarySpec.getSources().iterator() ) )
			return false;

		return this.isValidSourceSets( binarySpec.getApplication().getSources().iterator() );

	}

	/**
	 */
	private boolean isValidSourceSets( Iterator<LanguageSourceSet> sourceSets )
	{
		while( sourceSets.hasNext() )
		{
			LanguageSourceSet sourceSet = sourceSets.next();

			if( !(sourceSet instanceof ISourceSet) )
				return false;

			if( !"haxe".equals( ((ISourceSet)sourceSet).getSourcePlatform().getName() ) )
				return false;
		}

		return true;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 * CONVERT
	 */
	@Override
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec )
	{
		return this.createHXML( binarySpec, "" );
	}

	/**
	 * COMPILE
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec )
	{
		return this.createGradle( binarySpec, "" );
	}

	/**
	 * TEST
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		String name = testSpec.getName();

		Task convert = this.createHXML( binarySpec, name );
		Task compile = this.createGradle( binarySpec, name );

		// ------------------------------------------- //

		String testTaskName  = NameUtil.getBinaryTaskName( binarySpec, "executeTest",  name );

		Task execute = TaskUtil.createTask( binarySpec, ExecuteJarTask.class, testTaskName, it ->
		{
			it.setJarFile( this.getBuildResultFile( binarySpec, name ) );
			it.setMain( testSpec.getMain() );
		} );

		compile.dependsOn( convert );
		execute.dependsOn( compile );

		// ------------------------------------------- //

		return execute;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //
	// HXML:

	/**
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, String taskName )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.getTargetVariantCombination();

		String generateTaskName = NameUtil.getBinaryTaskName( binarySpec, "generateHxml", taskName );
		String executeTaskName  = NameUtil.getBinaryTaskName( binarySpec, "executeHxml",  taskName );
		File generateOutputDir  = this.getOutputDirectory( binarySpec, taskName );

		// ------------------------------------------- //
		// hxml:

		//
		GenerateHXMLTask generateTask = TaskUtil.createTask( binarySpec, GenerateHXMLTask.class, generateTaskName, it ->
		{
			it.setTargetVariantCombination( targetVariation );
			it.setOptions( binarySpec.getOptions() );   // TODO: tests might not use these options ... ?

			it.setOutputDir( generateOutputDir );
		} );

		//
		ExecuteHXMLTask executeTask = TaskUtil.createTask( binarySpec, ExecuteHXMLTask.class, executeTaskName, it ->
		{
			it.setHxmlFile( generateTask.getHxmlFile() );
		} );

		//
		executeTask.dependsOn( generateTask );

		// ------------------------------------------- //

		return executeTask;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	private Task createGradle( IApplicationBinarySpec binarySpec, String taskName )
	{
		String generateTaskName = NameUtil.getBinaryTaskName( binarySpec, "generateGradleProject", taskName );
		String executeTaskName  = NameUtil.getBinaryTaskName( binarySpec, "executeGradleProject",  taskName );
		File generateOutputDir  = this.getOutputDirectory( binarySpec, taskName );

		// ------------------------------------------- //
		// gradle:

		//
		GenerateGradleTask generateTask = TaskUtil.createTask( binarySpec, GenerateGradleTask.class, generateTaskName, it ->
		{
			it.setOutputDir( generateOutputDir );

			it.getGradleFile();
			it.getSettingsFile();
		} );

		//
		ExecuteGradleTask executeTask = TaskUtil.createTask( binarySpec, ExecuteGradleTask.class, executeTaskName, it ->
		{
			it.setGradleFile( generateTask.getGradleFile() );
		} );

		// ------------------------------------------- //

		executeTask.dependsOn( generateTask );

		return executeTask;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

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