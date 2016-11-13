package at.dotpoint.gradle.haxe.task.java;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.Arrays;

/**
 * Created by RK on 21.05.2016.
 */
public class ExecuteJarTask extends DefaultTask
{
	//
	private File jarFile;
	private String main;

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 */
	public File getJarFile()
	{
		return this.jarFile;
	}

	/**
	 */
	public void setJarFile( File jarFile )
	{
		this.jarFile = jarFile;
	}

	public String getMain()
	{
		return main;
	}

	public void setMain( String main )
	{
		this.main = main;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

    @TaskAction
    public void executeJar()
    {
	    this.getProject().exec( it ->
	    {
		    String javaPath = System.getenv("JAVA_HOME");

            if( System.getProperty("os.name").toLowerCase().contains( "win" ) )
                javaPath += "/bin/java.exe";

		    // ----------- //

		    it.setWorkingDir( ExecuteJarTask.this.getJarFile().getParentFile().getAbsoluteFile() );

			it.setExecutable( javaPath );
			it.setArgs( Arrays.asList( "-jar", ExecuteJarTask.this.getJarFile().getAbsolutePath(), this.getMain() ) );
	    } );
    }
}
