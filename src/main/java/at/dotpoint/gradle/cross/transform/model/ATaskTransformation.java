package at.dotpoint.gradle.cross.transform.model;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.artifacts.ArtifactDependencyResolver;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.resolve.ProjectModelResolver;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 08.07.2016.
 */
public abstract class ATaskTransformation<TTarget extends ITaskTransformationData> implements ITaskTransformation<TTarget>
{
	// general
	private ProjectFinder projectFinder;

	// external dependency - module
	private ArtifactDependencyResolver artifactDependencyResolver;
	private DependencyHandler dependencyHandler;
	private RepositoryHandler repositoryHandler;

	// internal dependency - library
	private ProjectModelResolver projectModelResolver;
	private LibraryBinaryResolver libraryBinaryResolver;

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	public ATaskTransformation( ServiceRegistry serviceRegistry )
	{
		// general
		this.projectFinder = serviceRegistry.get( ProjectFinder.class );

		// external dependency - module
		this.artifactDependencyResolver = serviceRegistry.get( ArtifactDependencyResolver.class );
		this.dependencyHandler = serviceRegistry.get( DependencyHandler.class );
		this.repositoryHandler = serviceRegistry.get( RepositoryHandler.class );

		// internal dependency - library
		this.projectModelResolver = serviceRegistry.get( ProjectModelResolver.class );
		this.libraryBinaryResolver = new LibraryBinaryResolver( projectModelResolver );
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// abstracts:

	/**
	 */
	abstract public boolean canTransform( IApplicationBinarySpec binarySpec );

	/**
	 */
	abstract public void createTransformTask( TTarget target );

	/**
	 */
	abstract public void updateTransformTask( TTarget target );

	// ***************************************************************** //
	// ***************************************************************** //
	// Helper:

	//
	protected Project getProject( IApplicationBinarySpec binarySpec )
	{
		return this.projectFinder.findProject( binarySpec.getProjectPath() );
	}

	//
	protected File getOutputDirectory( IApplicationBinarySpec binarySpec, String prefix, String postFix  )
	{
		return new File( this.getProject( binarySpec ).getBuildDir(),
				NameUtil.getBinaryTaskName( binarySpec, prefix, postFix ) );
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// internal dependency - library:

	/**
	 */
	protected List<IApplicationBinarySpec> getLibraryDependencies( List<ISourceSet> sourceSets,
	                                                               VariantCombination<IVariant> targetVariation )
	{
		List<IApplicationBinarySpec> dependencies = new ArrayList<>();

		for( ISourceSet sourceSet : sourceSets )
		{
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
		}

		return dependencies;
	}

	// ***************************************************************** //
	// ***************************************************************** //
	// external dependency - module:

	/**
	 */
	protected Set<File> getModuleArtifacts( IApplicationBinarySpec binarySpec, List<ISourceSet> sourceSets )
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
			if( !( dependencySpec instanceof IModuleDependencySpec ) )
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
