package at.dotpoint.gradle.cross.dependency.resolver;

import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.internal.resolve.ProjectModelResolver;
import org.gradle.model.internal.registry.ModelRegistry;
import org.gradle.platform.base.BinarySpec;
import org.gradle.platform.base.ComponentSpecContainer;
/**
 * Created by RK on 2016-08-21.
 */
public class LibraryBinaryResolver
{
	//
	protected ProjectModelResolver projectModelResolver;

	//
	LibraryBinaryResolver( ProjectModelResolver projectModelResolver )
	{
		this.projectModelResolver = projectModelResolver
	}

	// ---------------------------------------- //
	// ---------------------------------------- //

	/**
	 *
	 * @param libraryDependencySpec
	 * @param variants
	 * @return
	 */
	public IApplicationBinarySpec resolveBinary( ILibraryDependencySpec libraryDependencySpec,
	                                             VariantCombination<IVariant> variants )
	{
		IApplicationComponentSpec library = this.findApplicationSpec( libraryDependencySpec );

		if( library == null )
			throw new RuntimeException("cannot resolve library: " + libraryDependencySpec );

		// ---------------- //

		for( BinarySpec binarySpec : library.binaries )
		{
			IApplicationBinarySpec applicationBinarySpec = null;

			if( binarySpec instanceof IApplicationBinarySpec )
				applicationBinarySpec = (IApplicationBinarySpec) binarySpec;

			if( applicationBinarySpec == null )
				continue;

			if( applicationBinarySpec.targetVariantCombination.contains( variants ) )
				return applicationBinarySpec;
		}

		return null;
	}

	/**
	 *
	 * @param libraryDependencySpec
	 * @return
	 */
	private IApplicationComponentSpec findApplicationSpec( ILibraryDependencySpec libraryDependencySpec )
	{
		ComponentSpecContainer components = this.findComponentSpecContainer( libraryDependencySpec );

		if( components == null )
			throw new RuntimeException("cannot resolve components: " + libraryDependencySpec );

		return components.withType( IApplicationComponentSpec.class ).get( libraryDependencySpec.getLibraryName() );
	}

	/**
	 *
	 * @param libraryDependencySpec
	 * @return
	 */
	private ComponentSpecContainer findComponentSpecContainer( ILibraryDependencySpec libraryDependencySpec )
	{
		ModelRegistry projectModel = findProject( libraryDependencySpec );

		if(  projectModel == null )
			throw new RuntimeException("cannot resolve project: " + libraryDependencySpec );

		return projectModel.find("components", ComponentSpecContainer.class);


	}

	/**
	 *
	 * @param libraryDependencySpec
	 * @return
	 */
	private ModelRegistry findProject( ILibraryDependencySpec libraryDependencySpec )
	{
		return projectModelResolver.resolveProjectModel( libraryDependencySpec.getProjectPath() );
	}
}
