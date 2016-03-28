package at.dotpoint.gradle.cross

import at.dotpoint.gradle.cross.sourceset.CrossLanguageTransform
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
import at.dotpoint.gradle.cross.util.StringUtil
import at.dotpoint.gradle.cross.variant.iterator.VariantContainer
import at.dotpoint.gradle.cross.variant.iterator.VariantIterator
import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.LibraryFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.VariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.flavor.executable.ExecutableFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.flavor.library.LibraryFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.platform.PlatformResolver
import org.gradle.api.Named
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.language.base.plugins.LanguageBasePlugin
import org.gradle.model.Each
import org.gradle.model.Finalize
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.model.internal.core.Hidden
import org.gradle.model.internal.manage.schema.ModelSchemaStore
import org.gradle.platform.base.ComponentBinaries
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.PlatformContainer
import org.gradle.platform.base.TypeBuilder
import org.gradle.platform.base.plugins.BinaryBasePlugin

import javax.inject.Inject

/**
 * Created by RK on 11.03.16.
 */
class CrossPlugin implements Plugin<Project>
{
	/**
	 *
	 */
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
		project.getPluginManager().apply(LanguageBasePlugin.class);
		project.getPluginManager().apply(BinaryBasePlugin.class);

		project.extensions.extraProperties.set( "LibraryComponentSpec", ILibraryComponentSpec );
		project.extensions.extraProperties.set( "ExecutableComponentSpec", IExecutableComponentSpec );
		project.extensions.extraProperties.set( "CrossSourceSet", ISourceSet );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 */
	@SuppressWarnings("UnusedDeclaration")
	//
	static class CrossPluginRules extends RuleSource
	{

		/**
		* IGeneralComponentSpec
		*/
		@ComponentType
		void registerGeneralComponentSpec( TypeBuilder<IGeneralComponentSpec> builder )
		{
			builder.defaultImplementation(GeneralComponentSpec.class);
			builder.internalView(IGeneralComponentSpecInternal.class);
		}

		/**
		 * ILibraryComponentSpec
		 */
		@ComponentType
		void registerLibraryComponentSpec( TypeBuilder<ILibraryComponentSpec> builder )
		{
			builder.defaultImplementation(LibraryComponentSpec.class);
			builder.internalView(ILibraryComponentSpecInternal.class);
		}

		/**
		 * IExecutableComponentSpec
		 */
		@ComponentType
		void registerExecutableComponentSpec( TypeBuilder<IExecutableComponentSpec> builder )
		{
			builder.defaultImplementation(ExecutableComponentSpec.class);
			builder.internalView(IExecutableComponentSpecInternal.class);
		}

		/**
		 * IApplicationBinarySpec
		 */
		@ComponentType
		void registerApplicationBinarySpec( TypeBuilder<IApplicationBinarySpec> builder )
		{
			builder.defaultImplementation(ApplicationBinarySpec.class);
			builder.internalView(IApplicationBinarySpecInternal.class);
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //

		/**
		 * VariantResolverRepository
		 */
		@Hidden @Model
		IVariantResolverRepository variationResolver()
		{
			return new VariantResolverRepository();
		}

		/**
		 * VariantResolver
		 */
		@Mutate
		public void registerVariationResolver( IVariantResolverRepository variantResolver, PlatformContainer platformContainer  )
		{
			variantResolver.register( new PlatformResolver( platformContainer ) );
			variantResolver.register( new ExecutableFlavorResolver() );
			variantResolver.register( new LibraryFlavorResolver() );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// source transformation:

		/**
		 * LanguageSourceSet
		 */
		@ComponentType
		void registerSourceSet( TypeBuilder<ISourceSet> builder )
		{
			builder.defaultImplementation(CrossSourceSet.class);
			builder.internalView(ISourceSetInternal.class);
		}

		@Mutate
		void registerLanguageTransform(LanguageTransformContainer languages, ServiceRegistry serviceRegistry)
		{
			ModelSchemaStore schemaStore = serviceRegistry.get(ModelSchemaStore.class);
			languages.add( new CrossLanguageTransform() );
		}

		@Finalize
		void assignSourceSetPlatforms(@Each ISourceSet sourceSet, IVariantResolverRepository variantResolver )
		{
			ISourceSetInternal languageSourceSet = (ISourceSetInternal) sourceSet;
			PlatformRequirement platformRequirement = languageSourceSet.getPlatformRequirement();

			if( platformRequirement != null )
			{
				Platform platform = variantResolver.resolve( IPlatform, platformRequirement );

				if( platform != null )
					languageSourceSet.setTargetPlatform( platform );
			}
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// generate binaries:

		/**
		 *
		 * @param builder
		 * @param applicationComponentSpec
		 * @param variantResolver
		 */
		@ComponentBinaries
		void generateApplicationBinaries(ModelMap<IApplicationBinarySpec> builder, IApplicationComponentSpec applicationComponentSpec,
										 IVariantResolverRepository variantResolver )
		{
			IApplicationComponentSpecInternal applicationComponentSpecInternal = (IApplicationComponentSpecInternal) applicationComponentSpec;
			VariantIterator<IVariantRequirement> iterator = new VariantIterator<>( applicationComponentSpecInternal.getVariantRequirements() );

			while( iterator.hasNext() )
			{
				VariantContainer<IVariantRequirement> permutation = iterator.next();

				builder.create( this.getVariationName( permutation ) ){ IApplicationBinarySpec binarySpec ->

					IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;

					this.setPlatformToBinarySpec( binarySpecInternal, variantResolver, permutation );
					this.setFlavorToBinarySpec( binarySpecInternal, variantResolver, permutation );
				}
			}
		}

		//
		private void setPlatformToBinarySpec( IApplicationBinarySpecInternal binarySpecInternal,
											  IVariantResolverRepository variantResolver,
											  VariantContainer<IVariantRequirement> permutation  )
		{
			PlatformRequirement platformRequirement= permutation.getVariant( PlatformRequirement.class );

			if( platformRequirement != null )
			{
				Platform platform = variantResolver.resolve( IPlatform, platformRequirement );

				if( platform != null )
					binarySpecInternal.setTargetPlatform( platform );
			}
		}

		//
		private void setFlavorToBinarySpec( IApplicationBinarySpecInternal binarySpecInternal,
											IVariantResolverRepository variantResolver,
											VariantContainer<IVariantRequirement> permutation )
		{
			LibraryFlavorRequirement libraryFlavorRequirement = permutation.getVariant( LibraryFlavorRequirement.class );

			if( libraryFlavorRequirement != null )
			{
				LibraryFlavor libraryFlavor = variantResolver.resolve( ILibraryFlavor, libraryFlavorRequirement );

				if( libraryFlavor != null )
					binarySpecInternal.setTargetFlavor( libraryFlavor );
			}

			// ----------------------------------- //

			ExecutableFlavorRequirement executableFlavorRequirement = permutation.getVariant( ExecutableFlavorRequirement.class );

			if( executableFlavorRequirement != null )
			{
				ExecutableFlavor executableFlavor = variantResolver.resolve( IExecutableFlavor, executableFlavorRequirement );

				if( executableFlavor != null )
					binarySpecInternal.setTargetFlavor( executableFlavor );
			}
		}

		/**
		 *
		 * @param permutation
		 * @return
		 */
		private String getVariationName( VariantContainer<Named> permutation )
		{
			ArrayList<String> names = new ArrayList<>();

			for (int i = 0; i < permutation.size(); i++) {
				names.add( permutation.get(i).name );
			}

			return StringUtil.toCamelCase( names );
		}
	}
}
