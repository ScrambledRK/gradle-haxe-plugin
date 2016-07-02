package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.task.DefaultCrossTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends DefaultCrossTask
{
    @TaskAction
    public void generateHXML()
    {
        println( "... generate HXML" );
        println( "" );
    }
}
