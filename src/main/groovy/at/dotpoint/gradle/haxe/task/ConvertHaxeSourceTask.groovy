package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.task.AConvertSourceTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by RK on 21.05.2016.
 */
class ConvertHaxeSourceTask extends AConvertSourceTask
{
    @TaskAction
    public void convertHaxeSource()
    {
        println( "... converting HaxeSource" );

        println( "    " + this.inputPlatform );
        println( "    " + this.outputPlatform );
    }
}
