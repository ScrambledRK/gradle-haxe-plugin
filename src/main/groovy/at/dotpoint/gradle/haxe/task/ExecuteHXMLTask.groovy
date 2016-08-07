package at.dotpoint.gradle.haxe.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by RK on 21.05.2016.
 */
class ExecuteHXMLTask extends DefaultTask
{
	//
	private File hxmlFile;

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 *
	 * @return
	 */
	public File getHxmlFile()
	{
		return this.hxmlFile
	}

	/**
	 *
	 * @param hxmlFile
	 */
	public void setHxmlFile( File hxmlFile )
	{
		this.hxmlFile = hxmlFile
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

    @TaskAction
    public void executeHXML()
    {
        project.exec
		{
			it.executable "haxe"
			it.args this.hxmlFile.absolutePath;
		}
    }
}
