package at.dotpoint.gradle.haxe.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends DefaultTask
{
    @TaskAction
    public void generateHXML()
    {
        println( "... generating HXML" );
    }
}
