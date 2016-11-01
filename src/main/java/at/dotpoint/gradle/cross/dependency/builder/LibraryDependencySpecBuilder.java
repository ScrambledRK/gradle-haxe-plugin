package at.dotpoint.gradle.cross.dependency.builder;

import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpecInternal;
import at.dotpoint.gradle.cross.dependency.model.LibraryDependencySpec;

/**
 * Created by RK on 2016-08-21.
 */
public class LibraryDependencySpecBuilder implements ILibraryDependencySpecBuilder
{
	protected String projectPath;
	protected String libraryName;

	@Override
	public String getProjectPath()
	{
		return projectPath
	}

	@Override
	public void setProjectPath( String projectPath )
	{
		this.projectPath = projectPath
	}

	@Override
	public String getLibraryName()
	{
		return libraryName
	}

	@Override
	public void setLibraryName( String libraryName )
	{
		this.libraryName = libraryName
	}

	@Override
	public ILibraryDependencySpec build()
	{
		ILibraryDependencySpecInternal specInternal = new LibraryDependencySpec();

		specInternal.projectPath = this.projectPath;
		specInternal.libraryName = this.libraryName;

		return specInternal;
	}
}
