package at.dotpoint.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.tasks.TaskResolver

/**
 * Created by RK on 13.02.16.
 */
class HaxeTaskResolver implements TaskResolver
{

    public Project project;

    @Override
    Task resolveTask( String s )
    {
        if( project == null )
            return null;

        return project.getTasksByName( s ).getAt( 0 );
    }

}
