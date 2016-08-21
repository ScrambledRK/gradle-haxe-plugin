package at.dotpoint.gradle.cross.dependency.container;

import at.dotpoint.gradle.cross.dependency.builder.*;
import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by RK on 2016-08-21.
 */
public class CrossDependencySpecContainer implements IDependencySpecContainer
{
	//
	protected List<IDependencySpecBuilder> builderList;

	/**
	 *
	 */
	public CrossDependencySpecContainer()
	{
		this.builderList = new ArrayList<IDependencySpecBuilder>();
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 * Defines a new dependency, based on a library name. The returned dependency can be mutated.
	 * a component library of this or another sub-project
	 *
	 * @param projectPath project of the referenced library
	 * @param libraryName name of the library
	 *
	 * @return a mutable dependency, added to this container
	 */
	@Override
	public ILibraryDependencySpecBuilder library( String projectPath, String libraryName )
	{
		ILibraryDependencySpecBuilder builder = this.createLibraryBuilder();

		builder.setProjectPath( projectPath );
		builder.setLibraryName( libraryName );

		this.builderList.add( builder );

		// ---------- //

		return builder;
	}

	/**
	 * Defines a new module dependency, based on a module id or a simple name. The returned dependency can be mutated.
	 * an external prebuilt artifact resolved from a repository
	 *
	 * @param group of the module
	 * @param module of the module
	 * @param version of the module
	 *
	 * @return a mutable module dependency, added to this container
	 */
	@Override
	public IModuleDependencySpecBuilder module( String group, String module, String version )
	{
		IModuleDependencySpecBuilder builder = this.createModuleBuilder();

		builder.setGroup( group );
		builder.setModule( module );
		builder.setVersion( version );

		this.builderList.add( builder );

		// ---------- //

		return builder;
	}

	/**
	 * Returns an immutable view of dependencies stored in this container.
	 *
	 * @return an immutable view of dependencies. Each dependency in the collection is itself immutable.
	 */
	@Override
	public Collection<IDependencySpec> getDependencies()
	{
		ImmutableSet.Builder<IDependencySpec> specs = ImmutableSet.builder();

        for( IDependencySpecBuilder specBuilder : this.builderList )
            specs.add( specBuilder.build() );

        return specs.build();
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	//
	protected ILibraryDependencySpecBuilder createLibraryBuilder()
	{
		return new LibraryDependencySpecBuilder();
	}

	//
	protected IModuleDependencySpecBuilder createModuleBuilder()
	{
		return new ModuleDependencySpecBuilder();
	}
}
