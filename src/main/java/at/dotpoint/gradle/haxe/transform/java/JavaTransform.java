package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransform;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
import at.dotpoint.gradle.cross.util.StringUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RK on 27.02.16.
 */
public class JavaTransform extends ALifeCycleTransform<JavaTransformData>
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
	JavaTransform( ProjectFinder projectFinder, DependencyHandler dependencyHandler,
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
	public JavaTransformData createTransformData()
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
		VariantCombination<IVariant> targetVariation = binarySpec.targetVariantCombination;

		if( targetVariation.platform.name != "java" )
			return false;

		if( !this.isValidSourceSets( binarySpec.sources.iterator() ) )
			return false;

		return this.isValidSourceSets( binarySpec.application.sources.iterator() );

	}

	/**
	 *
	 * @param sourceSets
	 * @return
	 */
	private boolean isValidSourceSets( Iterator<LanguageSourceSet> sourceSets )
	{
		for( LanguageSourceSet sourceSet : sourceSets )
		{
			if( !(sourceSet instanceof ISourceSet) )
				return false;

			if( sourceSet.sourcePlatform.name != "haxe" )
				return false;
		}

		return true;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //
	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	@Override
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec,
	                                            JavaTransformData input )
	{
		return this.createHXML( binarySpec, input, "compile" );
	}

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                            JavaTransformData input )
	{
		return this.createGradle( binarySpec, input, "compile" );
	}

	/**
	 *
	 * @param testBinary binary testing the sourceBinary
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec,
	                                         JavaTransformData input )
	{
		return null;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	private Task createHXML( IApplicationBinarySpec binarySpec, JavaTransformData input, String sourceSetName )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.targetVariantCombination;
		List<ISourceSet> sourceSets = this.getSourceSets( binarySpec, sourceSetName );

		// ------------------------------------------- //
		// hxml:

		//
		Task generateTask = TaskUtil.createTask( binarySpec, GenerateHXMLTask.class,
				StringUtil.toCamelCase( binarySpec.tasks.taskName( "generateHxml" ), sourceSetName ) )
		{
			it.targetVariantCombination = binarySpec.targetVariantCombination.clone();

			it.options = binarySpec.options;
			it.sourceSets = sourceSets;

			it.outputDir = new File( it.project.buildDir, binarySpec.tasks.taskName( sourceSetName ) );
		}

		//
		Task executeTask = TaskUtil.createTask( binarySpec, ExecuteHXMLTask.class,
				StringUtil.toCamelCase( binarySpec.tasks.taskName( "executeHxml" ), sourceSetName ) )
		{
			it.hxmlFile = (generateTask as GenerateHXMLTask).hxmlFile;
		}

		// ------------------------------------------- //
		// dependencies:

		for( ISourceSet set : sourceSets )
		{
			List<IApplicationBinarySpec> libraries = this.getLibraryDependencies( set, targetVariation );
			List<Configuration> artifacts = this.getArtifactDependencies( binarySpec, set );

			for( IApplicationBinarySpec dependency : libraries )
				generateTask.dependsOn dependency.buildTask;

			for( Configuration configuration : artifacts )
			{
				Set<File> files = configuration.resolve();
				(generateTask as GenerateHXMLTask).dependencies = files;
			}
		}

		// ------------------------------------------- //

		executeTask.dependsOn generateTask;

		return executeTask;
	}

	/**
	 *
	 * @return
	 */
	private Task createGradle( IApplicationBinarySpec binarySpec, JavaTransformData input, String sourceSetName )
	{
		//
		Task generateTask = TaskUtil.createTask( binarySpec, GenerateGradleTask.class,
				StringUtil.toCamelCase( binarySpec.tasks.taskName( "generateGradleProject" ), sourceSetName ) )
		{
			it.outputDir = new File( it.project.buildDir, binarySpec.tasks.taskName( sourceSetName ) );

			it.getGradleFile();
			it.getSettingsFile();
		}

		//
		Task executeTask = TaskUtil.createTask( binarySpec, ExecuteGradleTask.class,
				StringUtil.toCamelCase( binarySpec.tasks.taskName( "executeGradleProject" ), sourceSetName ) )
		{
			it.gradleFile = (generateTask as GenerateGradleTask).gradleFile;
		}

		// ------------------------------------------- //

		executeTask.dependsOn generateTask;

		return executeTask;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 * @return
	 */
	private List<ISourceSet> getSourceSets( IApplicationBinarySpec binarySpec,
	                                        String name )
	{
		List<ISourceSet> directSourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		List<ISourceSet> specificSourceSets = new ArrayList<ISourceSet>();

		for( ISourceSet set : directSourceSets )
		{
			if( set.name == name )
				specificSourceSets.add( set );
		}

		return specificSourceSets;
	}

	/**
	 *
	 * @param sourceSet
	 * @param targetVariation
	 */
	private List<IApplicationBinarySpec> getLibraryDependencies( ISourceSet sourceSet,
	                                                             VariantCombination<IVariant> targetVariation )
	{
		List<IApplicationBinarySpec> dependencies = new ArrayList<>();

		for( IDependencySpec dependencySpec : sourceSet.dependencies.dependencies )
		{
			if( dependencySpec instanceof ILibraryDependencySpec )
			{
				ILibraryDependencySpec libraryDependencySpec = (ILibraryDependencySpec)dependencySpec;
				IApplicationBinarySpec applicationBinarySpec = this.libraryBinaryResolver.resolveBinary(
						libraryDependencySpec, targetVariation );

				if( applicationBinarySpec != null )
					dependencies.add( applicationBinarySpec );
			}
		}

		return dependencies;
	}

	/**
	 *
	 * @param sourceSet
	 * @return
	 */
	private List<Configuration> getArtifactDependencies( IApplicationBinarySpec binarySpec, ISourceSet sourceSet )
	{
		Iterable<IDependencySpec> dependencies = sourceSet.dependencies.dependencies;
		List<Configuration> configurationList = new ArrayList<>();

		for( IDependencySpec dependencySpec : dependencies )
		{
			if( !(dependencySpec instanceof IModuleDependencySpec) )
				continue;

			Project project = this.projectFinder.findProject( binarySpec.getProjectPath() );
			Dependency dependency = this.dependencyHandler.create( dependencySpec.getDisplayName() );

			ConfigurationContainer configurationContainer = project.getConfigurations()
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