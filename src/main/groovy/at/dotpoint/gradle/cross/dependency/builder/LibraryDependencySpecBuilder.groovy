package at.dotpoint.gradle.cross.dependency.builder

import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpecInternal
import at.dotpoint.gradle.cross.dependency.model.LibraryDependencySpec

/**
 * Created by RK on 2016-08-21.
 */
class LibraryDependencySpecBuilder implements ILibraryDependencySpecBuilder
{
	protected String projectPath;
	protected String libraryName;

	@Override
	String getProjectPath()
	{
		return projectPath
	}

	@Override
	void setProjectPath( String projectPath )
	{
		this.projectPath = projectPath
	}

	@Override
	String getLibraryName()
	{
		return libraryName
	}

	@Override
	void setLibraryName( String libraryName )
	{
		this.libraryName = libraryName
	}

	@Override
	ILibraryDependencySpec build()
	{
		ILibraryDependencySpecInternal specInternal = new LibraryDependencySpec();

		specInternal.projectPath = this.projectPath;
		specInternal.libraryName = this.libraryName;

		return specInternal;
	}
}
