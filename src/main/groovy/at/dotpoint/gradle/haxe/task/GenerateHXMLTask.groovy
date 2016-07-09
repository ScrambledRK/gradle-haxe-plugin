package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.task.AConvertTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends AConvertTask
{
    @TaskAction
    public void generateHXML()
    {
        println( "... generate HXML: " + this.sourceVariantCombination.platform + " to " + this.targetVariantCombination.platform );
        println( "" );
    }
}
