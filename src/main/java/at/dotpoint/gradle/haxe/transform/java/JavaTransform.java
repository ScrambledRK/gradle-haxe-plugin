package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransform;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformData;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
import at.dotpoint.gradle.cross.util.StringUtil;
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
import java.util.stream.Collectors;

/**
 * Created by RK on 27.02.16.
 */
public class JavaTransform extends ALifeCycleTransform
{

	//
	private LibraryBinaryResolver libraryBinaryResolver;

	//
	private ArtifactDependencyResolver artifactDependencyResolver;

	//
	private RepositoryHandler repositoryHandler;

	//
	private ProjectFinder projectFinder;

	//
	private DependencyHandler dependencyHandler;

	//
	public JavaTransform( ProjectFinder projectFinder, DependencyHandler dependencyHandler,
			       LibraryBinaryResolver libraryBinaryResolver )
	{
		this.projectFinder = projectFinder;
		this.dependencyHandler = dependencyHandler;
		this.libraryBinaryResolver = libraryBinaryResolver;
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

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	@Override
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec,
	                                            ILifeCycleTransformData input )
	{
		return this.createHXML( binarySpec, input, BinarySpecUtil.getSourceSetList( binarySpec ), "" );
	}

	/**
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                            ILifeCycleTransformData input )
	{
		return this.createGradle( binarySpec, input, "" );
	}

	/**
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec,
	                                         ITestComponentSpec testSpec, ILifeCycleTransformData input )
	{
		//String name = testSpec.getName();

		//Task convert = this.createHXML( binarySpec, input, BinarySpecUtil.getSourceSetList( testSpec ), name );
		//Task compile = this.createGradle( binarySpec, input, name );

		// ------------------------------------------- //

		return null;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, ILifeCycleTransformData input,
	                         List<ISourceSet> sourceSets, String taskName )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.getTargetVariantCombination();

		if( sourceSets.isEmpty() )
			System.out.println( binarySpec + " has no sourceSets for " + taskName );

		// ------------------------------------------- //
		// hxml:

		//
		GenerateHXMLTask generateTask = TaskUtil.createTask( binarySpec, GenerateHXMLTask.class,
				GenerateHXMLTask.generateTaskName( binarySpec, taskName ), it ->
		{
			it.setTargetVariantCombination( targetVariation );
			it.setOptions( binarySpec.getOptions() );

			it.setSourceSets( sourceSets );
			it.setDependencies( this.getArtifactFiles( binarySpec, sourceSets ) );

			it.setOutputDir( new File( it.getProject().getBuildDir(),
					binarySpec.getTasks().taskName( taskName ) ) );

			this.getDependencyBuildTasks( sourceSets, targetVariation ).forEach( it::dependsOn );
		} );

		//
		ExecuteHXMLTask executeTask = TaskUtil.createTask( binarySpec, ExecuteHXMLTask.class,
				ExecuteHXMLTask.generateTaskName( binarySpec, taskName ), it ->
		{
			it.setHxmlFile( generateTask.getHxmlFile() );
		} );

		//
		executeTask.dependsOn( generateTask );

		// ------------------------------------------- //

		return executeTask;
	}

	/**
	 */
	private Task createGradle( IApplicationBinarySpec binarySpec, ILifeCycleTransformData input, String name )
	{
		//
		GenerateGradleTask generateTask = TaskUtil.createTask( binarySpec, GenerateGradleTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "generateGradleProject" ), name ), it ->
		{
			it.setOutputDir( new File( it.getProject().getBuildDir(), binarySpec.getTasks().taskName( name ) ) );

			it.getGradleFile();
			it.getSettingsFile();
		} );

		//
		ExecuteGradleTask executeTask = TaskUtil.createTask( binarySpec, ExecuteGradleTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "executeGradleProject" ), name ), it ->
		{
			it.setGradleFile( generateTask.getGradleFile() );
		} );

		// ------------------------------------------- //

		executeTask.dependsOn( generateTask );

		return executeTask;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

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
	private List<Task> getDependencyBuildTasks( List<ISourceSet> sourceSets,
	                                            VariantCombination<IVariant> targetVariation )
	{
		List<Task> dependencyTasks = new ArrayList<>();

		for( ISourceSet set : sourceSets )
		{
			List<IApplicationBinarySpec> libraries = this.getLibraryDependencies( set, targetVariation );

			dependencyTasks.addAll( libraries.stream()
					.map( IApplicationBinarySpec::getBuildTask )
					.collect( Collectors.toList() ) );
		}

		return dependencyTasks;
	}

	/**
	 */
	private List<ISourceSet> getSourceSets( IApplicationBinarySpec binarySpec,
	                                        String name )
	{
		List<ISourceSet> sourceSetList = BinarySpecUtil.getSourceSetList( binarySpec );

		return sourceSetList.stream()
				.filter( set -> set.getName().equals( name ) )
				.collect( Collectors.toList() );
	}

	/**
	 */
	private List<IApplicationBinarySpec> getLibraryDependencies( ISourceSet sourceSet,
	                                                             VariantCombination<IVariant> targetVariation )
	{
		List<IApplicationBinarySpec> dependencies = new ArrayList<>();

		sourceSet.getDependencies().getDependencies().stream()
				.filter( dependencySpec -> dependencySpec instanceof ILibraryDependencySpec )
				.forEach( dependencySpec ->
				{
					ILibraryDependencySpec libraryDependencySpec = (ILibraryDependencySpec) dependencySpec;
					IApplicationBinarySpec applicationBinarySpec = this.libraryBinaryResolver.resolveBinary(
							libraryDependencySpec, targetVariation );

					if( applicationBinarySpec != null )
						dependencies.add( applicationBinarySpec );
				} );

		return dependencies;
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