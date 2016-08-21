package at.dotpoint.gradle.cross.dependency.model

import com.google.common.base.Joiner

import static com.google.common.collect.Lists.newArrayList

/**
 * Created by RK on 2016-08-21.
 */
class LibraryDependencySpec implements ILibraryDependencySpecInternal
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
	String getDisplayName()
	{
		List<String> parts = newArrayList();

        if (getProjectPath() != null) {
            parts.add("project '" + getProjectPath() + "'");
        }
        if (getLibraryName() != null) {
            parts.add("library '" + getLibraryName() + "'");
        }
        return Joiner.on(' ').join(parts);
	}
}
