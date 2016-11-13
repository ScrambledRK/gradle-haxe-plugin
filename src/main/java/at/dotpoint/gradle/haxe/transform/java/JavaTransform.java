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
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec )
	{
		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );

		if( sourceSets.isEmpty() )
		{
			System.out.println( binarySpec + " has no sourceSets" );
			return null;
		}

		return this.createHXML( binarySpec, sourceSets, "" );
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
		//String name = testSpec.getName();

		//Task convert = this.createHXML( binarySpec, input, BinarySpecUtil.getSourceSetList( testSpec ), name );
		//Task compile = this.createGradle( binarySpec, input, name );

		// ------------------------------------------- //

		return null;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //
	// HXML:

	/**
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, List<ISourceSet> sourceSets, String taskName )
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
			it.setOptions( binarySpec.getOptions() );

			it.setSourceSets( sourceSets );
			it.setOutputDir( generateOutputDir );

			it.setDependencies( this.getArtifactFiles( binarySpec, sourceSets ) );
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