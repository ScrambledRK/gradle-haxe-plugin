package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.task.APlatformTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends APlatformTask
{
    @TaskAction
    public void generateHXML()
    {
        println( "... generate HXML" );
        println( "" );
    }
}
