package at.dotpoint.gradle.haxe.task.java;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by RK on 21.05.2016.
 */
public class ExecuteGradleTask extends DefaultTask
{
	//
	private File gradleFile;

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 *
	 * @return
	 */
	public File getGradleFile()
	{
		return this.gradleFile
	}

	/**
	 *
	 * @param hxmlFile
	 */
	public void setGradleFile( File hxmlFile )
	{
		this.gradleFile = hxmlFile
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

    @TaskAction
    public void executeHXML()
    {
	    String gradlePath = System.getenv("gradle")

	    if( System.getProperty("os.name").toLowerCase().contains( "win" ) )
		    gradlePath += "/gradle.bat"

        project.exec
		{
			it.workingDir this.gradleFile.parentFile.absolutePath

			it.executable gradlePath
			it.args "build";
		}
    }
}
