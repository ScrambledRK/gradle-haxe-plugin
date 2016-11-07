package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
		return this.createHXML( binarySpec, input, "compile" );
	}

	/**
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                            ILifeCycleTransformData input )
	{
		return this.createGradle( binarySpec, input, "compile" );
	}

	/**
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec,
	                                         ILifeCycleTransformData input )
	{
		return null;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, ILifeCycleTransformData input, String sourceSetName )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.getTargetVariantCombination();
		List<ISourceSet> sourceSets = this.getSourceSets( binarySpec, sourceSetName );

		if( sourceSets.isEmpty() )
			System.out.println( binarySpec + " has no sourceSets for " + sourceSetName );

		// ------------------------------------------- //
		// hxml:

		//
		GenerateHXMLTask generateTask = TaskUtil.createTask( binarySpec, GenerateHXMLTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "generateHxml" ), sourceSetName ), it ->
		{
			it.setTargetVariantCombination( targetVariation );

			it.setOptions( binarySpec.getOptions() );
			it.setSourceSets( sourceSets );

			it.setOutputDir( new File( it.getProject().getBuildDir(), binarySpec.getTasks().taskName( sourceSetName ) ) );
		} );

		//
		ExecuteHXMLTask executeTask = TaskUtil.createTask( binarySpec, ExecuteHXMLTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "executeHxml" ), sourceSetName ), it ->
		{
			it.setHxmlFile( generateTask.getHxmlFile() );
		} );

		//
		executeTask.dependsOn( generateTask );

		// ------------------------------------------- //
		// dependencies:

		for( ISourceSet set : sourceSets )
		{
			List<IApplicationBinarySpec> libraries = this.getLibraryDependencies( set, targetVariation );
			List<Configuration> artifacts = this.getArtifactDependencies( binarySpec, set );

			for( IApplicationBinarySpec dependency : libraries )
				generateTask.dependsOn( dependency.getBuildTask() );

			for( Configuration configuration : artifacts )
			{
				Set<File> files = configuration.resolve();
				//generateTask.dependsOn( files );
			}
		}

		// ------------------------------------------- //

		return executeTask;
	}

	/**
	 */
	private Task createGradle( IApplicationBinarySpec binarySpec, ILifeCycleTransformData input, String sourceSetName )
	{
		//
		GenerateGradleTask generateTask = TaskUtil.createTask( binarySpec, GenerateGradleTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "generateGradleProject" ), sourceSetName ), it ->
		{
			it.setOutputDir( new File( it.getProject().getBuildDir(), binarySpec.getTasks().taskName( sourceSetName ) ) );

			it.getGradleFile();
			it.getSettingsFile();
		} );

		//
		ExecuteGradleTask executeTask = TaskUtil.createTask( binarySpec, ExecuteGradleTask.class,
				StringUtil.toCamelCase( binarySpec.getTasks().taskName( "executeGradleProject" ), sourceSetName ), it ->
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
	private List<ISourceSet> getSourceSets( IApplicationBinarySpec binarySpec,
	                                        String name )
	{
		List<ISourceSet> directSourceSets = BinarySpecUtil.getSourceSetList( binarySpec );

		return directSourceSets.stream()
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

	//
//	private List<ResolutionAwareRepository> getResolutionAwareRepositories()
//	{
//		List<ResolutionAwareRepository> resolutionAwareRepositories = new ArrayList<>();
//
//		for( ArtifactRepository repository : this.repositoryHandler.collect() )
//		{
//			if( repository instanceof ResolutionAwareRepository )
//				resolutionAwareRepositories.add( repository );
//		}
//
//		return resolutionAwareRepositories;
//	}
}