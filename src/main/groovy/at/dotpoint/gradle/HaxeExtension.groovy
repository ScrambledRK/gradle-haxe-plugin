package at.dotpoint.gradle

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.internal.tasks.DefaultSourceSetContainer
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.api.file.SourceDirectorySet

/**
 * Created by RK on 12.02.16.
 */
class HaxeExtension
{
    //
    public static final String NAME = "haxe";

    //
    private Project project;

    //
    private SourceSetContainer sourceSetContainer;

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    public HaxeExtension( Project project, HaxePlugin plugin )
    {
        this.project = project;

        this.sourceSetContainer = new DefaultSourceSetContainer( plugin.fileResolver, plugin.taskResolver, plugin.instantiator );
        this.sourceSetContainer.doCreate( "main" );
    }

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    public SourceSetContainer sourceSetContainer( Closure config )
    {
        return this.sourceSetContainer.configure( config );
    }

    public SourceSetContainer getSourceSetContainer()
    {
        return this.sourceSetContainer;
    }
}
