package at.dotpoint.gradle.cross.dependency.builder

import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec
/**
 * Created by RK on 2016-08-21.
 */
interface ILibraryDependencySpecBuilder extends IDependencySpecBuilder
{

	String getProjectPath()

	void setProjectPath( String projectPath )

	String getLibraryName()

	void setLibraryName( String libraryName )

	@Override
	ILibraryDependencySpec build();
}
