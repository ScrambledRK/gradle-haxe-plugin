package at.dotpoint.gradle.cross.dependency.model;

/**
 * Created by RK on 2016-08-21.
 */
public interface ILibraryDependencySpec extends IDependencySpec
{
	String getProjectPath();

	String getLibraryName();
}

public interface ILibraryDependencySpecInternal extends ILibraryDependencySpec
{

	void setProjectPath( String projectPath );

	void setLibraryName( String libraryName );
}

