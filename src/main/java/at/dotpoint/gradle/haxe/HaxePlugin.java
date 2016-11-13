package at.dotpoint.gradle.haxe;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver;
import at.dotpoint.gradle.cross.transform.builder.ITransformBuilder;
import at.dotpoint.gradle.cross.transform.builder.lifecycle.LifeCycleTransformationBuilder;
import at.dotpoint.gradle.cross.transform.container.LifeCycleTransformationContainer;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransform;
import at.dotpoint.gradle.cross.transform.repository.ITransformBuilderRepository;
import at.dotpoint.gradle.haxe.transform.java.JavaTransform;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.resolve.ProjectModelResolver;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;

import javax.inject.Inject;
/**
 *  Created by RK on 28.03.2016.
 */
public class HaxePlugin implements Plugin<Project>
{

	/**
	 *
	 */
	public final Instantiator instantiator;
	public final FileResolver fileResolver;

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	@Inject
	public HaxePlugin( Instantiator instantiator, FileResolver fileResolver )
	{
		this.instantiator = instantiator;
		this.fileResolver = fileResolver;
	}

	@Override
	public void apply( final Project project )
	{
		project.getPluginManager().apply( CrossPlugin.class );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 */
	static class HaxePluginRules extends RuleSource
	{

		// -------------------------------------------------- //
		// -------------------------------------------------- //

		@Mutate
		void registerTransformBuilder( final ITransformBuilderRepository transforms,
		                               ServiceRegistry serviceRegistry )
		{
			transforms.add( this.createLifeCycleTransformBuilder( serviceRegistry ) );
		}

		/**
		 */
		private ITransformBuilder createLifeCycleTransformBuilder( ServiceRegistry serviceRegistry )
		{
			LifeCycleTransformationContainer container = new LifeCycleTransformationContainer();

			container.add( this.createJavaTransform( serviceRegistry ) );

			return new LifeCycleTransformationBuilder( container );
		}

		/**
		 */
		private ILifeCycleTransform createJavaTransform( ServiceRegistry serviceRegistry )
		{
			ProjectFinder projectFinder                     = serviceRegistry.get( ProjectFinder.class );
//			ArtifactDependencyResolver dependencyResolver   = serviceRegistry.get( ArtifactDependencyResolver.class )
//			RepositoryHandler repositoryHandler             = serviceRegistry.get( RepositoryHandler.class )

			ProjectModelResolver projectModelResolver       = serviceRegistry.get( ProjectModelResolver.class );
			DependencyHandler dependencyHandler             = serviceRegistry.get( DependencyHandler.class );

			LibraryBinaryResolver libraryBinaryResolver = new LibraryBinaryResolver( projectModelResolver );

			return new JavaTransform( projectFinder, dependencyHandler, libraryBinaryResolver );
		}

	}
}
