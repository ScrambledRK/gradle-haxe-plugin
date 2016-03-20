package at.dotpoint.gradle.cross

import at.dotpoint.gradle.cross.specification.*
import at.dotpoint.gradle.cross.specification.executable.ExecutableComponentSpec
import at.dotpoint.gradle.cross.specification.executable.IExecutableComponentSpec
import at.dotpoint.gradle.cross.specification.executable.IExecutableComponentSpecInternal
import at.dotpoint.gradle.cross.specification.library.ILibraryComponentSpec
import at.dotpoint.gradle.cross.specification.library.ILibraryComponentSpecInternal
import at.dotpoint.gradle.cross.specification.library.LibraryComponentSpec
import at.dotpoint.gradle.cross.variant.iterator.VariantContainer
import at.dotpoint.gradle.cross.variant.iterator.VariantIterator
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.VariantResolverRepository
import at.dotpoint.gradle.cross.variant.resolver.flavor.executable.ExecutableFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.flavor.library.LibraryFlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.platform.PlatformResolver
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.language.base.plugins.LanguageBasePlugin
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.model.internal.core.Hidden
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

		/**
		 *
		 * @param builder
		 * @param libraryComponentSpec
		 * @param variantResolver
		 */
		@ComponentBinaries
		void generateLibraryBinaries( ModelMap<IApplicationBinarySpec> builder, ILibraryComponentSpec libraryComponentSpec,
										  IVariantResolverRepository variantResolver )
		{
			ILibraryComponentSpecInternal libraryComponentSpecInternal = (ILibraryComponentSpecInternal) libraryComponentSpec;

			println( libraryComponentSpecInternal.getVariantRequirements() )

			VariantIterator<IVariantRequirement> iterator = new VariantIterator<>( libraryComponentSpecInternal.getVariantRequirements() );

			while( iterator.hasNext() )
			{
				VariantContainer<IVariantRequirement> permutation = iterator.next();

				PlatformRequirement platformRequirement= permutation.getVariant( PlatformRequirement.class );

				println( permutation );
				println( platformRequirement );

				if( platformRequirement != null )
				{
					IPlatform platform = variantResolver.resolve( IPlatform, platformRequirement );

					builder.create( platform.name ){ IApplicationBinarySpec binarySpec ->

						IApplicationBinarySpecInternal binarySpecInternal = (IApplicationBinarySpecInternal) binarySpec;

						binarySpecInternal.setTargetPlatform( platform );
					}
				}
			}
		}
	}
}
