package at.dotpoint.gradle.haxe.task.neko;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.Collections;

/**
 * Created by RK on 21.05.2016.
 */
public class ExecuteNekoTask extends DefaultTask
{
	//
	private File nekoFile;
	private String main;

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 */
	public File getNekoFile()
	{
		return this.nekoFile;
	}

	/**
	 */
	public void setNekoFile( File nekoFile )
	{
		this.nekoFile = nekoFile;
	}


	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

    @TaskAction
    public void executeNeko()
    {
	    System.out.println( ExecuteNekoTask.this.getNekoFile().getAbsolutePath() );

	    this.getProject().exec( it ->
	    {
		    String executablePath = System.getenv("NEKOPATH");

            if( System.getProperty("os.name").toLowerCase().contains( "win" ) )
                executablePath += "/neko.exe";

		    // ----------- //

		    it.setWorkingDir( ExecuteNekoTask.this.getNekoFile().getParentFile().getAbsoluteFile() );

			it.setExecutable( executablePath );
			it.setArgs( Collections.singletonList( ExecuteNekoTask.this.getNekoFile().getAbsolutePath() ) );
	    } );
    }
}
