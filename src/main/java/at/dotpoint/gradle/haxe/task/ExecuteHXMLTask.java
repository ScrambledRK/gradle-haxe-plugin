package at.dotpoint.gradle.haxe.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

	// ********************************************************************************************** //
	// ********************************************************************************************** //

    @TaskAction
    public void executeHXML() throws IOException
    {
	    this.executeConversion();
	    this.moveTempFiles();
    }

	/**
	 */
	private void executeConversion()
	{
		this.getProject().exec( it ->
	    {
		    String haxePath = System.getenv("HAXEPATH");

	        if( System.getProperty("os.name").toLowerCase().contains( "win" ) )
	            haxePath += "haxe.exe";

	        // ----------- //

	        it.setExecutable( "cmd" );
	        it.setArgs( Arrays.asList( "/C", "haxe", ExecuteHXMLTask.this.getHxmlFile().getAbsolutePath() ) );
	    } );
	}

	/**
	 *
	 * @throws IOException
	 */
	private void moveTempFiles() throws IOException
	{
		File baseDir = this.getHxmlFile().getParentFile();
		File buildDir = new File( baseDir, "build" );

		new File( baseDir, "cmd" ).delete();
		new File( baseDir, "hxjava_build.txt" ).delete();
		new File( baseDir, "manifest" ).delete();
		new File( baseDir, baseDir.getName() + ".jar" ).delete();
	}

}
