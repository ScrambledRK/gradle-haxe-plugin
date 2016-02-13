package at.dotpoint.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.TaskResolver
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

/**
 * Created by RK on 12.02.16.
 */
class HaxePlugin implements Plugin<Project>
{
    /**
     *
     */
    private HaxeExtension extension;

    /**
     *
     */
    public final Instantiator instantiator;
    public final FileResolver fileResolver;
    public final TaskResolver taskResolver;

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    @Inject
    public HaxePlugin( Instantiator instantiator, FileResolver fileResolver )
    {
        this.instantiator = instantiator;
        this.fileResolver = fileResolver;
        this.taskResolver = new HaxeTaskResolver();
    }

    @Override
    public void apply( final Project project )
    {
        this.extension = this.createExtension( project );
        this.taskResolver.project = project;
    }

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    private HaxeExtension createExtension( Project project )
    {
        return project.extensions.create( HaxeExtension.NAME, HaxeExtension, project, this );
    }
}
