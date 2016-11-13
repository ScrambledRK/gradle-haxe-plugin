package at.dotpoint.gradle.haxe.task;

import at.dotpoint.gradle.cross.util.StringUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.platform.base.BinarySpec;

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

	//
	public static String generateTaskName( BinarySpec binarySpec, String postFix )
	{
		return StringUtil.toCamelCase( binarySpec.getTasks().taskName( "executeHxml" ), postFix );
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

		this.moveFileTo( baseDir, buildDir, "cmd" );
		this.moveFileTo( baseDir, buildDir, "hxjava_build.txt" );
		this.moveFileTo( baseDir, buildDir, "manifest" );
		this.moveFileTo( baseDir, buildDir, baseDir.getName() + ".jar" );
	}

	/**
	 * @throws IOException
	 */
	private void moveFileTo( File base, File target, String name ) throws IOException
	{
		target.mkdirs();

		boolean success = new File( base, name ).renameTo( new File( target, name ) );

		if( !success )
			System.out.println( "could not move file: " + base + "/" + name + " to " + target );
	}
}
