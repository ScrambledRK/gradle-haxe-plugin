package at.dotpoint.gradle.cross

import at.dotpoint.gradle.cross.configuration.builder.ConfigurationBuilder
import at.dotpoint.gradle.cross.configuration.model.Configuration
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirementInternal
import at.dotpoint.gradle.cross.sourceset.CrossSourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.sourceset.ISourceSetInternal
import at.dotpoint.gradle.cross.specification.*
import at.dotpoint.gradle.cross.transform.builder.ITransformBuilder
import at.dotpoint.gradle.cross.transform.repository.ITransformBuilderRepository
import at.dotpoint.gradle.cross.transform.repository.TransformBuilderRepository
import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.variant.container.buildtype.BuildTypeContainer
import at.dotpoint.gradle.cross.variant.container.buildtype.IBuildTypeContainer
import at.dotpoint.gradle.cross.variant.container.flavor.FlavorContainer
import at.dotpoint.gradle.cross.variant.container.flavor.IFlavorContainer
import at.dotpoint.gradle.cross.variant.factory.buildtype.BuildTypeFactory
import at.dotpoint.gradle.cross.variant.factory.flavor.FlavorFactory
import at.dotpoint.gradle.cross.variant.factory.platform.PlatformFactory
import at.dotpoint.gradle.cross.variant.iterator.VariantIterator
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.VariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.buildtype.BuildTypeResolver
import at.dotpoint.gradle.cross.variant.resolver.flavor.FlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.platform.PlatformResolver
import at.dotpoint.gradle.cross.variant.target.VariantCombination
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
import org.gradle.platform.base.internal.BinarySpecInternal

import javax.inject.Inject
/**
 *  Created by RK on 11.03.16.
 */
class CrossPlugin implements Plugin<Project>
{
	/**
	 *
	 */
	public static final String NAME_CONVERT_SOURCE 	= "convert"
	public static final String NAME_COMPILE_SOURCE 	= "compile"
	public static final String NAME_TEST_SOURCE 	= LifecycleBasePlugin.CHECK_TASK_NAME
	public static final String NAME_PACKAGE_SOURCE 	= "package"
	public static final String NAME_INSTALL_SOURCE 	= "install"
	public static final String NAME_EXECUTE_SOURCE 	= "execute"
	public static final String NAME_ASSEMBLE 		= LifecycleBasePlugin.ASSEMBLE_TASK_NAME;
	public static final String NAME_BUILD 		    = LifecycleBasePlugin.BUILD_TASK_NAME;

	public static final String GROUP_NAME_BUILD 	    = LifecycleBasePlugin.BUILD_GROUP;
	public static final String GROUP_NAME_TEST 	        = LifecycleBasePlugin.VERIFICATION_GROUP;
	public static final String GROUP_NAME_DISTRIBUTE    = "distribute";
	public static final String GROUP_NAME_EXECUTE       = "execute";

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

		project.extensions.extraProperties.set( "IApplicationComponent", IApplicationComponentSpec );
		project.extensions.extraProperties.set( "ISourceSet",   ISourceSet );
		project.extensions.extraProperties.set( "IPlatform",    IPlatform );
		project.extensions.extraProperties.set( "IFlavor",      IFlavor );
		project.extensions.extraProperties.set( "IBuildType",   IBuildType );
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

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
		 * IApplicationComponentSpec
		 */
		@ComponentType
		void registerApplicationComponentSpec( TypeBuilder<IApplicationComponentSpec> builder )
		{
			builder.defaultImplementation( ApplicationComponentSpec.class );
			builder.internalView( IApplicationComponentSpecInternal.class );
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

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 * ConvertTransform, CompileTransform
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class TransformRules extends RuleSource
	{
		/**
		 * ITransformBuilderRepository
		 */
		@Hidden @Model
		ITransformBuilderRepository transformBuilderRepository()
		{
			return new TransformBuilderRepository();
		}

		/**
		 * create ConvertTransformTasks for BinarySpecs
		 */
		@Finalize
		void createTransformTasks( final TaskContainer taskContainer,
		                           @Path("binaries") final ModelMap<BinarySpecInternal> binaries,
		                           ITransformBuilderRepository transformBuilderRepository )
		{

			for( BinarySpecInternal binarySpec : binaries )
			{
				if( !(binarySpec instanceof IApplicationBinarySpecInternal ) )
					continue;

				for( ITransformBuilder builder : transformBuilderRepository )
					builder.createTransformationTasks( (IApplicationBinarySpecInternal) binarySpec );
			}
		}
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

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
		void generateApplicationBinaries( ModelMap<IApplicationBinarySpec> builder,
		                                  IApplicationComponentSpec applicationComponentSpec,
										  IVariantResolverRepository variantResolver,
										  @Path('buildDir') File buildDir )
		{
			IApplicationComponentSpecInternal applicationComponentSpecInternal =
					(IApplicationComponentSpecInternal) applicationComponentSpec;

			VariantIterator<IVariantRequirement> iterator = new VariantIterator<>(
					applicationComponentSpecInternal.getVariantRequirements() );

			// ----------------------------- //
			// create binary permutations according to ApplicationComponent variants

			while( iterator.hasNext() )
			{
				VariantCombination<IVariantRequirement> permutation = iterator.next();

				//
				this.createBinarySpec( builder, applicationComponentSpecInternal, permutation,
						variantResolver, buildDir );
			}
		}

		/**
		 *
		 * @param applicationComponentSpecInternal
		 * @param permutation
		 * @return
		 */
		private IApplicationBinarySpecInternal createBinarySpec(
													ModelMap<IApplicationBinarySpec> builder,
													IApplicationComponentSpecInternal applicationComponentSpec,
													VariantCombination<IVariantRequirement> permutation,
													IVariantResolverRepository variantResolver,
													File buildDir )
		{
			String variationName = NameUtil.getVariationName( permutation );

			//
			builder.create( variationName ) { IApplicationBinarySpec binarySpec ->

				IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;

				this.setTargetVariant( binarySpecInternal, permutation, variantResolver );
				this.setConfiguration( binarySpecInternal, applicationComponentSpec, buildDir );
			}

			return (IApplicationBinarySpecInternal) builder.get( variationName );
		}

		/**
		 *
		 * @param binarySpec
		 * @param permutation
		 */
		private void setTargetVariant( IApplicationBinarySpecInternal binarySpec,
		                               VariantCombination<IVariantRequirement> permutation,
		                               IVariantResolverRepository variantResolver )
		{
			for( IVariantRequirement requirement : permutation )
			{
				IVariantRequirement variantRequirement = (IVariantRequirement) permutation.getVariant(
						requirement.getClass() );

				IVariant variant = variantResolver.resolve( variantRequirement.getVariantType(),
						variantRequirement );

				binarySpec.setTargetVariant( variant );
			}
		}

		/**
		 *
		 * @param binarySpec
		 * @param applicationComponentSpec
		 * @param buildDir
		 */
		private void setConfiguration( IApplicationBinarySpecInternal binarySpec,
				                       IApplicationComponentSpecInternal applicationComponentSpec,
				                       File buildDir )
		{
			Configuration configuration = this.generateConfigurationRequirement( binarySpec,
					applicationComponentSpec, buildDir );

			if( configuration != null )
				binarySpec.setConfiguration( configuration );
		}

		/**
		 *
		 * @param binarySpec
		 * @param applicationComponentSpec
		 * @param buildDir
		 * @return
		 */
		private Configuration generateConfigurationRequirement( IApplicationBinarySpecInternal binarySpec,
		                                                        IApplicationComponentSpecInternal applicationComponentSpec,
		                                                        File buildDir )
		{
			VariantCombination<IVariant> variantCombination = binarySpec.getTargetVariantCombination();
			ArrayList<IConfigurationRequirementInternal> requirements = new ArrayList<>();

			for( IVariant variant : variantCombination )
			{
				if( variant.configuration != null )
					requirements.add( variant.configuration as IConfigurationRequirementInternal );
			}

			if( applicationComponentSpec.configuration != null )
				requirements.add( applicationComponentSpec.configuration as IConfigurationRequirementInternal );

			return new ConfigurationBuilder( buildDir ).build( requirements );
		}
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

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
		                                IFlavorContainer flavorContainer,
		                                IBuildTypeContainer buildTypeContainer )
		{
			variantResolver.register( new PlatformResolver( platformContainer ) );
			variantResolver.register( new FlavorResolver( flavorContainer ) );
			variantResolver.register( new BuildTypeResolver( buildTypeContainer ) );
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
			flavorContainer.registerFactory( IFlavor.class, new FlavorFactory() );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// Variant:BuildType

		/**
		 * IBuildTypeContainer
		 */
		@SuppressWarnings("GroovyAssignabilityCheck")
		//
		@Model
		IBuildTypeContainer buildTypes()
		{
			return new BuildTypeContainer()
		}

		/**
		 * IBuildType Factories
		 */
		@Mutate
		void registerBuildTypeFactories( IBuildTypeContainer buildTypeContainer )
		{
			buildTypeContainer.registerFactory( IBuildType.class, new BuildTypeFactory() );
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

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 * LifeCycle: assemble, compile, convert; check, build
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class LifeCycleRules extends RuleSource
	{

		/**
		 *
		 */
		@Mutate
		void updateDefaultBinaryTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			this.createLifeCycleTasks( tasks, binaries );

			for( BinarySpec binary : binaries )
			{
				this.removeBaseBinaryTasks( tasks, binary );
				this.createBinaryTasks( tasks, binary );
			}

			this.copyBinaryTasksToTaskContainer( tasks, binaries );
			this.updateBinaryTaskDependencies( tasks, binaries );
		}

		// ---------------------------------------- //
		// ---------------------------------------- //

		/**
		 * custom LifeCycle tasks: compile, convert; (assemble)
		 */
		private void createLifeCycleTasks( TaskContainer tasks, BinaryContainer binaries )
		{
			tasks.create( NAME_CONVERT_SOURCE, DefaultTask.class )
			{
				it.group = GROUP_NAME_BUILD;
				it.description = "converts non-native sources to the native target language of a binary"
			}

			tasks.create( NAME_COMPILE_SOURCE, DefaultTask.class )
			{
				it.group = GROUP_NAME_BUILD;
				it.description = "compiles native sources to a native target binary"
			}

//			tasks.create( NAME_TEST_SOURCE, DefaultTask.class )         // "check" task already provided
//			{                                                           // just like "assemble" or "build" is
//				it.group = GROUP_NAME_TEST;
//				it.description = "tests native sources after they've been compiled"
//			}

			// -------------------- //

			tasks.create( NAME_PACKAGE_SOURCE, DefaultTask.class )
			{
				it.group = GROUP_NAME_DISTRIBUTE;
				it.description = "packs an assembled product into a distribution form (zip etc.)"
			}

			tasks.create( NAME_INSTALL_SOURCE, DefaultTask.class )
			{
				it.group = GROUP_NAME_EXECUTE;
				it.description = "unpacks installs a packaged product into an executable form"
			}

			tasks.create( NAME_EXECUTE_SOURCE, DefaultTask.class )
			{
				it.group = GROUP_NAME_EXECUTE;
				it.description = "runs/executes an installed product"
			}

			// -------------------- //

			tasks.getByName(NAME_COMPILE_SOURCE).dependsOn NAME_CONVERT_SOURCE;
			tasks.getByName(NAME_TEST_SOURCE).dependsOn NAME_COMPILE_SOURCE;

			tasks.getByName(NAME_ASSEMBLE).dependsOn NAME_COMPILE_SOURCE;
			tasks.getByName(NAME_ASSEMBLE).dependsOn NAME_PACKAGE_SOURCE;

			// -------------------- //

			tasks.getByName(NAME_PACKAGE_SOURCE).dependsOn NAME_COMPILE_SOURCE;
			tasks.getByName(NAME_INSTALL_SOURCE).dependsOn NAME_PACKAGE_SOURCE;
			tasks.getByName(NAME_EXECUTE_SOURCE).dependsOn NAME_INSTALL_SOURCE;
		}

		/**
		 * remove default tasks created by the BinaryBasePlugin
		 */
		private void removeBaseBinaryTasks( TaskContainer tasks, BinarySpec binary )
		{
			Task buildTask = binary.getBuildTask();

			if( buildTask != null )
			{
				tasks.remove( buildTask );
				binary.tasks.remove( buildTask );
			}
		}

		/**
		 * hook-tasks for each binary, plugins can depend on these
		 */
		private void createBinaryTasks( TaskContainer tasks, BinarySpec binary )
		{
			Task taskConvert 	= null;
			Task taskCompile 	= null;
			Task taskTest 	    = null;
			Task taskAssemble 	= null;

			Task taskPackage 	= null;
			Task taskInstall 	= null;
			Task taskExecute 	= null;

			Task taskBuild 	= null;

			//
			binary.tasks.create( binary.tasks.taskName( NAME_CONVERT_SOURCE ), DefaultTask.class )
			{
				taskConvert = it;
			}

			//
			binary.tasks.create( binary.tasks.taskName( NAME_COMPILE_SOURCE ), DefaultTask.class )
			{
				taskCompile = it;
			}

			//
			binary.tasks.create( binary.tasks.taskName( NAME_TEST_SOURCE ), DefaultTask.class )
			{
				taskTest = it;
			}

			//
			binary.tasks.create( binary.tasks.taskName( NAME_ASSEMBLE ), DefaultTask.class )
			{
				taskAssemble = it;
			}

			// --------- //

			//
			binary.tasks.create( binary.tasks.taskName( NAME_PACKAGE_SOURCE ), DefaultTask.class )
			{
				taskPackage = it;
			}

			//
			binary.tasks.create( binary.tasks.taskName( NAME_INSTALL_SOURCE ), DefaultTask.class )
			{
				taskInstall = it;
			}

			//
			binary.tasks.create( binary.tasks.taskName( NAME_EXECUTE_SOURCE ), DefaultTask.class )
			{
				taskExecute = it;
			}

			// --------- //

			//
			binary.tasks.create( binary.tasks.taskName( NAME_BUILD ), DefaultTask.class )
			{
				taskBuild = it;
			}

			// --------- //

			taskCompile.dependsOn taskConvert;
			taskTest.dependsOn taskCompile;

			taskAssemble.dependsOn taskCompile;
			taskAssemble.dependsOn taskPackage;

			binary.setBuildTask( taskAssemble );

			// --------- //

			taskPackage.dependsOn taskCompile;
			taskInstall.dependsOn taskPackage;
			taskExecute.dependsOn taskInstall;

			// --------- //

			taskBuild.dependsOn taskAssemble;

			if( taskTest != null )
				taskBuild.dependsOn taskTest;
		}

		/**
		 * assign binary specific tasks to LifeCycle tasks
		 */
		private void updateBinaryTaskDependencies(TaskContainer tasks, BinaryContainer binaries )
		{
			Task convertLifeCycleTask = tasks.getByName(NAME_CONVERT_SOURCE);
			Task compileLifeCycleTask = tasks.getByName(NAME_COMPILE_SOURCE);
			Task testLifeCycleTask    = tasks.getByName(NAME_TEST_SOURCE);

			Task packageLifeCycleTask = tasks.getByName(NAME_PACKAGE_SOURCE);
			Task installLifeCycleTask = tasks.getByName(NAME_INSTALL_SOURCE);
			Task executeLifeCycleTask = tasks.getByName(NAME_EXECUTE_SOURCE);

			Task buildLifeCycleTask = tasks.getByName(NAME_BUILD);

			//
			for( BinarySpec binary : binaries )
			{
				for( Task task in binary.getTasks() )
				{
					if( task.name.startsWith(NAME_CONVERT_SOURCE) )
						convertLifeCycleTask.dependsOn task;

					if( task.name.startsWith(NAME_COMPILE_SOURCE) )
						compileLifeCycleTask.dependsOn task;

					if( task.name.startsWith(NAME_TEST_SOURCE) )
						testLifeCycleTask.dependsOn task;

					// --------- //

					if( task.name.startsWith(NAME_PACKAGE_SOURCE) )
						packageLifeCycleTask.dependsOn task;

					if( task.name.startsWith(NAME_INSTALL_SOURCE) )
						installLifeCycleTask.dependsOn task;

					if( task.name.startsWith(NAME_EXECUTE_SOURCE) )
						executeLifeCycleTask.dependsOn task;

					// --------- //

					if( task.name.startsWith(NAME_BUILD) )
						buildLifeCycleTask.dependsOn task;
				}
			}
		}

		/**
		 * make binary tasks visible in root task container
		 */
		private void copyBinaryTasksToTaskContainer( TaskContainer tasks, BinaryContainer binaries )
		{
			for( BinarySpec binary : binaries )
			{
				tasks.addAll( binary.getTasks() );
				Task buildTask = binary.getBuildTask();

				if( buildTask != null )
					tasks.add( buildTask );
			}
		}

		// ---------------------------------------- //
		// ---------------------------------------- //

		/**
		 * clear assemble task dependencies, assign custom LifeCycleTasks
		 */
		@Finalize
		void updateAssembleDependencies( @Path("tasks.assemble") Task assemble, BinaryContainer binaries )
		{
			(DefaultTaskDependency)(assemble.getTaskDependencies()).getValues().clear();

			for( BinarySpec binary : binaries )
			{
				for( Task task in binary.getTasks() )
				{
					if( task.name.startsWith(NAME_ASSEMBLE) )
						assemble.dependsOn task;
				}
			}

			assemble.dependsOn NAME_COMPILE_SOURCE;
			assemble.dependsOn NAME_PACKAGE_SOURCE;
		}

	}
}
