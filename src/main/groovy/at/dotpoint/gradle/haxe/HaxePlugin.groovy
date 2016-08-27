package at.dotpoint.gradle.haxe

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver
import at.dotpoint.gradle.cross.transform.container.CompileTransformationContainer
import at.dotpoint.gradle.cross.transform.container.ConvertTransformationContainer
import at.dotpoint.gradle.haxe.sourceset.HaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSet
import at.dotpoint.gradle.haxe.sourceset.IHaxeSourceSetInternal
import at.dotpoint.gradle.haxe.specification.HaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpec
import at.dotpoint.gradle.haxe.specification.IHaxeBinarySpecInternal
import at.dotpoint.gradle.haxe.transform.HaxeCompileTransform
import at.dotpoint.gradle.haxe.transform.HaxeConvertTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.resolve.ProjectModelResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

import javax.inject.Inject
/**
 *  Created by RK on 28.03.2016.
 */
class HaxePlugin implements Plugin<Project>
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

		//JavaPluginConvention javaConvention = new JavaPluginConvention(project, instantiator);
  		//project.getConvention().getPlugins().put("java", javaConvention);
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 */
	@SuppressWarnings(["UnusedDeclaration", "GrMethodMayBeStatic"])
	//
	static class HaxePluginRules extends RuleSource
	{

		/**
		 * HaxeComponentSpec
		 */
		@ComponentType
		void registerHaxeBinarySpec( TypeBuilder<IHaxeBinarySpec> builder )
		{
			builder.defaultImplementation( HaxeBinarySpec.class );
			builder.internalView( IHaxeBinarySpecInternal.class );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //
		// source transformation:

		/**
		 * LanguageSourceSet
		 */
		@ComponentType
		void registerSourceSet( TypeBuilder<IHaxeSourceSet> builder )
		{
			builder.defaultImplementation( HaxeSourceSet.class );
			builder.internalView( IHaxeSourceSetInternal.class );
		}

		// -------------------------------------------------- //
		// -------------------------------------------------- //

		@Mutate
		void registerConvertTransform( ConvertTransformationContainer transforms, ServiceRegistry serviceRegistry )
		{
			ProjectModelResolver projectModelResolver = serviceRegistry.get( ProjectModelResolver.class );
			LibraryBinaryResolver libraryBinaryResolver = new LibraryBinaryResolver( projectModelResolver );

			transforms.add( new HaxeConvertTransform( libraryBinaryResolver ) );
		}

		@Mutate
		void registerCompileTransform( CompileTransformationContainer transforms, ServiceRegistry serviceRegistry )
		{
			ProjectModelResolver projectModelResolver = serviceRegistry.get( ProjectModelResolver.class );
			LibraryBinaryResolver libraryBinaryResolver = new LibraryBinaryResolver( projectModelResolver );

			transforms.add( new HaxeCompileTransform( libraryBinaryResolver ) );
		}

	}
}
