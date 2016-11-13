package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransform;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformData;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
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
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.artifacts.ArtifactDependencyResolver;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.language.base.LanguageSourceSet;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by RK on 27.02.16.
 */
public class JavaTransform extends ALifeCycleTransform
{

	//
	private ArtifactDependencyResolver artifactDependencyResolver;

	//
	private RepositoryHandler repositoryHandler;

	//
	private ProjectFinder projectFinder;

	//
	private DependencyHandler dependencyHandler;

	//
	public JavaTransform( ProjectFinder projectFinder, DependencyHandler dependencyHandler )
	{
		this.projectFinder = projectFinder;
		this.dependencyHandler = dependencyHandler;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	@Override
	public ILifeCycleTransformData createTransformData()
	{
		return new JavaTransformData();
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 * SourceSet to convert
	 */
	@Override
	protected boolean isValidTransformTarget( IApplicationBinarySpec binarySpec )
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
	 */
	@Override
	protected void setBuildResult( IApplicationBinarySpec binarySpec )
	{
		binarySpec.getBuildResult().add( this.getBuildResultFile( binarySpec, "" ) );
	}

	/**
	 */
	@Override
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec )
	{
		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		Set<File> dependencies = this.getArtifactFiles( binarySpec, sourceSets );

		return this.createHXML( binarySpec, sourceSets, dependencies, "" );
	}

	/**
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec )
	{
		return this.createGradle( binarySpec, "" );
	}

	/**
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		String name = testSpec.getName();

		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( testSpec );
		Set<File> dependencies = this.getTestDependencies( binarySpec, testSpec );

		Task convert = this.createHXML( binarySpec, sourceSets, dependencies, name );
		Task compile = this.createGradle( binarySpec, name );

		Task execute = TaskUtil.createTask( binarySpec, ExecuteJarTask.class, "executeTest", it ->
		{
			it.setJarFile( this.getBuildResultFile( binarySpec, name ) );
			it.setMain( testSpec.getMain() );
		} );

		compile.dependsOn( convert );
		execute.dependsOn( compile );

		// ------------------------------------------- //

		return execute;
	}

	/**
	 */
	private Set<File> getTestDependencies( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		Set<File> dependencies = new HashSet<>();

		// test dependencies
		dependencies.addAll( this.getArtifactFiles( binarySpec, BinarySpecUtil.getSourceSetList( testSpec ) ) );

		// binary dependencies
		dependencies.addAll( this.getArtifactFiles( binarySpec, BinarySpecUtil.getSourceSetList( binarySpec ) ) );

		// binary itself
		dependencies.addAll( binarySpec.getBuildResult()
				.stream().collect( Collectors.toList() ) );

		return dependencies;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //
	// HXML:

	/**
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, List<ISourceSet> sourceSets, Set<File> dependencies,
	                         String taskName )
	{
		if( sourceSets.isEmpty() )
			throw new RuntimeException( "cannot create HXML because sourceSet is empty" );

		// ------------------------------------------- //

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
			it.setOptions( binarySpec.getOptions() );   // TODO: tests might not use these options ...

			it.setSourceSets( sourceSets );
			it.setOutputDir( generateOutputDir );

			it.setDependencies( dependencies );
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
	private Project getProject( IApplicationBinarySpec binarySpec )
	{
		return this.projectFinder.findProject( binarySpec.getProjectPath() );
	}

	//
	private File getOutputDirectory( IApplicationBinarySpec binarySpec, String name )
	{
		return new File( this.getProject( binarySpec ).getBuildDir(),
						NameUtil.getBinaryTaskName( binarySpec, name ) );
	}

	//
	private File getBuildResultFile( IApplicationBinarySpec binarySpec, String name )
	{
		File libraryDir = new File( this.getOutputDirectory( binarySpec, name ), "build/libs" );
		String fileName = this.getBuildResultName( binarySpec );

		return new File( libraryDir, fileName );
	}

	private String getBuildResultName( IApplicationBinarySpec binarySpec )
	{
		Project project = this.getProject( binarySpec );

		String version = project.getVersion().toString();
		String name = project.getName();


		return name + "-" + version + ".jar";
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	private Set<File> getArtifactFiles( IApplicationBinarySpec binarySpec, List<ISourceSet> sourceSets )
	{
		Set<File> dependencyArtifacts = new HashSet<>();

		for( ISourceSet set : sourceSets )
		{
			List<Configuration> artifacts = this.getArtifactDependencies( binarySpec, set );

			for( Configuration configuration : artifacts )
				dependencyArtifacts.addAll( configuration.resolve() );
		}

		return dependencyArtifacts;
	}

	/**
	 */
	private List<Configuration> getArtifactDependencies( IApplicationBinarySpec binarySpec, ISourceSet sourceSet )
	{
		Iterable<IDependencySpec> dependencies = sourceSet.getDependencies().getDependencies();
		List<Configuration> configurationList = new ArrayList<>();

		for( IDependencySpec dependencySpec : dependencies )
		{
			if( !(dependencySpec instanceof IModuleDependencySpec) )
				continue;

			Project project = this.projectFinder.findProject( binarySpec.getProjectPath() );
			Dependency dependency = this.dependencyHandler.create( dependencySpec.getDisplayName() );

			ConfigurationContainer configurationContainer = project.getConfigurations();
			Configuration configuration = configurationContainer.detachedConfiguration( dependency );

			configurationList.add( configuration );
		}

		return configurationList;
	}

}