package at.dotpoint.gradle

import org.gradle.api.Project

/**
 * Created by RK on 12.02.16.
 */
class HaxeExtension
{
    //
    public static final String NAME = "haxe";

    //
    private Project project;

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    public HaxeExtension( Project project, HaxePlugin plugin )
    {
        this.project = project;
    }

}
