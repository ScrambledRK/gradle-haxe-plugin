package at.dotpoint.gradle.cross.dependency.container;

import at.dotpoint.gradle.cross.dependency.builder.ILibraryDependencySpecBuilder;
import at.dotpoint.gradle.cross.dependency.builder.IModuleDependencySpecBuilder;
import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import org.gradle.model.internal.core.UnmanagedStruct;

import java.util.Collection;

/**
 * Created by RK on 2016-08-21.
 */
@UnmanagedStruct
//
public interface IDependencySpecContainer
{
	/**
	 * Defines a new dependency, based on a library name. The returned dependency can be mutated.
	 * a component library of this or another sub-project
	 *
	 * @param projectPath project of the referenced library
	 * @param libraryName name of the library
	 *
	 * @return a mutable dependency, added to this container
	 */
	ILibraryDependencySpecBuilder library( String projectPath, String libraryName );

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
	IModuleDependencySpecBuilder module( String group, String module, String version );

	/**
	 * Returns an immutable view of dependencies stored in this container.
	 *
	 * @return an immutable view of dependencies. Each dependency in the collection is itself immutable.
	 */
	Collection<IDependencySpec> getDependencies();
}