package at.dotpoint.gradle.cross

import at.dotpoint.gradle.cross.sourceset.CrossSourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.*
import at.dotpoint.gradle.cross.specification.executable.ExecutableComponentSpec
import at.dotpoint.gradle.cross.specification.executable.IExecutableComponentSpec
import at.dotpoint.gradle.cross.specification.executable.IExecutableComponentSpecInternal
import at.dotpoint.gradle.cross.specification.library.ILibraryComponentSpec
import at.dotpoint.gradle.cross.specification.library.ILibraryComponentSpecInternal
import at.dotpoint.gradle.cross.specification.library.LibraryComponentSpec
import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.variant.container.flavor.FlavorContainer
import at.dotpoint.gradle.cross.variant.container.flavor.IFlavorContainer
import at.dotpoint.gradle.cross.variant.factory.flavor.ExecutableFlavorFactory
import at.dotpoint.gradle.cross.variant.factory.flavor.LibraryFlavorFactory
import at.dotpoint.gradle.cross.variant.factory.platform.PlatformFactory
import at.dotpoint.gradle.cross.variant.iterator.VariantContainer
import at.dotpoint.gradle.cross.variant.iterator.VariantIterator
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.VariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.flavor.executable.ExecutableFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.flavor.library.LibraryFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.platform.PlatformResolver
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.tasks.TaskContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.language.base.plugins.LanguageBasePlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.model.*
import org.gradle.model.internal.core.Hidden
import org.gradle.platform.base.*

import javax.inject.Inject

/**
 *  Created by RK on 11.03.16.
 */
class CrossPlugin implements Plugin<Project>
{
	/**
	 *
	 */
	public static final String NAME_TRANSPILE_SOURCE 	= "transpile"
	public static final String NAME_COMPILE_SOURCE 		= "compile"
	public static final String NAME_CONVERT_ASSETS 		= "convert"

	public final Instantiator instantiator;
	public final FileResolver fileResolver;

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	@Inject
	public CrossPlugin( Instantiator instantiator, FileResolver fileResolver )
	{
		this.instantiator = instantiator;
		this.fileResolver = fileResolver;
	}

	@Override
	public void apply( final Project project )
	{
		project.getPluginManager().apply( LanguageBasePlugin.class );

		project.extensions.extraProperties.set( "LibraryComponentSpec", ILibraryComponentSpec );
		project.extensions.extraProperties.set( "ExecutableComponentSpec", IExecutableComponentSpec );
		project.extensions.extraProperties.set( "CrossSourceSet", ISourceSet );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //


	/**
	 * ComponentSpecs, SourceSets
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class ComponentRules extends RuleSource
	{
		/**
		 * IGeneralComponentSpec
		 */
		@ComponentType
		void registerGeneralComponentSpec( TypeBuilder<IGeneralComponentSpec> builder )
		{
			builder.defaultImplementation( GeneralComponentSpec.class );
			builder.internalView( IGeneralComponentSpecInternal.class );
		}

		/**
		 * ILibraryComponentSpec
		 */
		@ComponentType
		void registerLibraryComponentSpec( TypeBuilder<ILibraryComponentSpec> builder )
		{
			builder.defaultImplementation( LibraryComponentSpec.class );
			builder.internalView( ILibraryComponentSpecInternal.class );
		}

		/**
		 * IExecutableComponentSpec
		 */
		@ComponentType
		void registerExecutableComponentSpec( TypeBuilder<IExecutableComponentSpec> builder )
		{
			builder.defaultImplementation( ExecutableComponentSpec.class );
			builder.internalView( IExecutableComponentSpecInternal.class );
		}

		/**
		 * IApplicationBinarySpec
		 */
		@ComponentType
		void registerApplicationBinarySpec( TypeBuilder<IApplicationBinarySpec> builder )
		{
			builder.defaultImplementation( ApplicationBinarySpec.class );
			builder.internalView( IApplicationBinarySpecInternal.class );
		}

		/**
		 * LanguageSourceSet
		 */
		@ComponentType
		void registerSourceSet( TypeBuilder<ISourceSet> builder )
		{
			builder.defaultImplementation( CrossSourceSet.class );
			builder.internalView( ISourceSetInternal.class );
		}
	}

	/**
	 * BinarySpecs, LanguageSourceSets
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class BinaryRules extends RuleSource
	{

		/**
		 * LanguageSourceSet <- Platforms
		 */
		@Mutate
		void assignSourceSetPlatforms( @Each ISourceSet sourceSet, IVariantResolverRepository variantResolver )
		{
			ISourceSetInternal languageSourceSet = (ISourceSetInternal) sourceSet;
			PlatformRequirement platformRequirement = languageSourceSet.getPlatformRequirement();

			if( platformRequirement != null && languageSourceSet.getSourcePlatform() == null )
			{
				Platform platform = (Platform) variantResolver.resolve( IPlatform, platformRequirement );

				if( platform != null )
					languageSourceSet.setSourcePlatform( platform );
			}
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// generate binaries:

		/**
		 * generate BinarySpec permutations (Platform,BuildType,Flavor)
		 */
		@ComponentBinaries
		void generateApplicationBinaries( ModelMap<IApplicationBinarySpec> builder, IApplicationComponentSpec applicationComponentSpec,
										  IVariantResolverRepository variantResolver )
		{
			IApplicationComponentSpecInternal applicationComponentSpecInternal = (IApplicationComponentSpecInternal) applicationComponentSpec;
			VariantIterator<IVariantRequirement> iterator = new VariantIterator<>( applicationComponentSpecInternal.getVariantRequirements() );

			while( iterator.hasNext() )
			{
				VariantContainer<IVariantRequirement> permutation = iterator.next();

				builder.create( NameUtil.getVariationName( permutation ) ) {IApplicationBinarySpec binarySpec ->

					IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;

					for( IVariantRequirement requirement : permutation )
					{
						IVariantRequirement variantRequirement = (IVariantRequirement) permutation.getVariant( requirement.getClass() );
						IVariant variant = variantResolver.resolve( variantRequirement.getVariantType(), variantRequirement );

						binarySpecInternal.setTargetVariant( variant );
					}
				}
			}
		}

	}

	/**
	 * Variations: Platform, BuildType, Flavor for BinarySpecs
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class VariantRules extends RuleSource
	{
		/**
		 * VariantResolverRepository
		 */
		@Hidden
		@Model
		IVariantResolverRepository variationResolver()
		{
			return new VariantResolverRepository();
		}

		/**
		 * VariantResolver
		 */
		@Mutate
		void registerVariationResolver( IVariantResolverRepository variantResolver,
										PlatformContainer platformContainer,
										IFlavorContainer flavorContainer )
		{
			variantResolver.register( new PlatformResolver( platformContainer ) );
			variantResolver.register( new ExecutableFlavorResolver( flavorContainer as Set<IExecutableFlavor> ) );
			variantResolver.register( new LibraryFlavorResolver( flavorContainer as Set<ILibraryFlavor> ) );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// Variant:Flavor

		/**
		 * IFlavorContainer
		 */
		@SuppressWarnings("GroovyAssignabilityCheck")
		//
		@Model
		IFlavorContainer flavors()
		{
			return new FlavorContainer()
		}

		/**
		 * Flavor Factories
		 */
		@Mutate
		void registerFlavorFactories( IFlavorContainer flavorContainer )
		{
			flavorContainer.registerFactory( ILibraryFlavor.class, new LibraryFlavorFactory() );
			flavorContainer.registerFactory( IExecutableFlavor.class, new ExecutableFlavorFactory() );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// Variant:Platform (almost gradle-native)

		/**
		 * Platform Factories
		 */
		@Mutate
		void registerPlatformFactories( PlatformContainer platformContainer )
		{
			platformContainer.registerFactory( IPlatform.class, new PlatformFactory() );
		}

	}


	/**
	 * Variations: Platform, BuildType, Flavor for BinarySpecs
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class LifeCycleRules extends RuleSource
	{
		/**
		 * create custom LifeCycleTasks
		 */
		@Mutate
		void createLifeCycleTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			tasks.create( NAME_TRANSPILE_SOURCE, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "converts non-native sources to the native target language of a binary"
			}

			tasks.create( NAME_COMPILE_SOURCE, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "compiles native sources to a native target binary"
			}

			tasks.create( NAME_CONVERT_ASSETS, DefaultTask.class )
			{
				it.group = LifecycleBasePlugin.BUILD_GROUP;
				it.description = "converts raw assets for the native target binary"
			}

			tasks.getByName(NAME_COMPILE_SOURCE).dependsOn NAME_TRANSPILE_SOURCE;
		}

		/**
		 * assign custom LifeCycleTasks to BinarySpecs
		 */
		@Mutate
		void assignBinaryTasksToLifeCycle( TaskContainer tasks, BinaryContainer binaries )
		{
			for( BinarySpec binary : binaries )
			{
				for( Task task in binary.getTasks() )
				{
					if( task.name.startsWith(NAME_TRANSPILE_SOURCE) )
						tasks.getByName(NAME_TRANSPILE_SOURCE).dependsOn task;

					if( task.name.startsWith(NAME_COMPILE_SOURCE) )
						tasks.getByName(NAME_COMPILE_SOURCE).dependsOn task;

					if( task.name.startsWith(NAME_CONVERT_ASSETS) )
						tasks.getByName(NAME_CONVERT_ASSETS).dependsOn task;
				}
			}
		}

		/**
		 * remove default BinarySpec buildBy tasks
		 */
		@Mutate
		void removeDefaultTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			for( BinarySpec binary : binaries )
			{
				Task buildTask = binary.getBuildTask();

				if( buildTask != null )
				{
					tasks.remove( buildTask );
					binary.tasks.remove( buildTask );
				}
			}
		}

		/**
		 * clear assemble task dependencies, assign custom LifeCycleTasks
		 */
		@Finalize
		void updateAssembleDependencies( @Path("tasks.assemble") Task task )
		{
			(DefaultTaskDependency)(task.getTaskDependencies()).getValues().clear();
			task.setDependsOn( [ NAME_COMPILE_SOURCE, NAME_CONVERT_ASSETS ] );
		}
	}
}
