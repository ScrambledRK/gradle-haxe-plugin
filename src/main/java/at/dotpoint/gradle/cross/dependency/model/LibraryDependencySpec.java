package at.dotpoint.gradle.cross.dependency.model;

import com.google.common.base.Joiner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by RK on 2016-08-21.
 */
public class LibraryDependencySpec implements ILibraryDependencySpecInternal
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
	public String getDisplayName()
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
