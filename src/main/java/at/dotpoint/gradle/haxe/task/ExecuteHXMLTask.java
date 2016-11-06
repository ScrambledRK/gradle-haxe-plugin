package at.dotpoint.gradle.haxe.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.Collections;

/**
 * Created by RK on 21.05.2016.
 */
public class ExecuteHXMLTask extends DefaultTask
{
	//
	private File hxmlFile;

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 */
	public File getHxmlFile()
	{
		return this.hxmlFile;
	}

	/**
	 */
	public void setHxmlFile( File hxmlFile )
	{
		this.hxmlFile = hxmlFile;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

    @TaskAction
    public void executeHXML()
    {
	    this.getProject().exec( it ->
        {
            it.setExecutable( "haxe" );
            it.setArgs( Collections.singletonList( ExecuteHXMLTask.this.getHxmlFile().getAbsolutePath() ) );
        } );
    }
}
