package at.dotpoint.gradle.haxe.task.java;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by RK on 2016-09-04.
 */
public class GenerateGradleTask extends DefaultTask
{
	//
	private File outputDir;

	private File gradleFile;
	private File settingFile;

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 *
	 * @return
	 */
	public File getOutputDir()
	{
		if( this.outputDir == null )
			this.outputDir = new File( this.project.buildDir.absolutePath );

		return this.outputDir;
	}

	public void setOutputDir( File outputDir )
	{
		this.outputDir = outputDir;
	}

	/**
	 *
	 * @return
	 */
	public File getGradleFile()
	{
		if( this.gradleFile == null )
			this.setGradleFile( new File( this.getOutputDir(), "build.gradle" ) );

		return this.gradleFile;
	}

	/**
	 *
	 * @param hxmlFile
	 */
	public void setGradleFile( File gradleFile )
	{
		this.gradleFile = gradleFile;
		this.outputs.file( this.getGradleFile() );
	}

	/**
	 *
	 * @return
	 */
	public File getSettingsFile()
	{
		if( this.settingFile == null )
			this.setSettingsFile( new File( this.getOutputDir(), "settings.gradle" ) );

		return this.settingFile;
	}

	/**
	 *
	 * @param hxmlFile
	 */
	public void setSettingsFile( File settingFile )
	{
		this.settingFile = settingFile;
		this.outputs.file( this.getSettingsFile() );
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 *
	 */
    @TaskAction
    public void generateGradleProject()
    {
		this.generateBuildGradle();
	    this.generateSettingGradle();
    }

	/**
	 *
	 */
	private void generateBuildGradle()
	{
		String output = getClass().getResource("/templates/build-java.gradle").openStream().text;

		output = output.replaceAll( "%GROUP%", this.project.getGroup().toString() );
		output = output.replaceAll( "%VERSION%", this.project.getVersion().toString() );

		// -------------- //

		if( !this.getGradleFile().exists() )
		{
			this.gradleFile.parentFile.mkdirs();
			this.gradleFile.createNewFile();
		}

		this.gradleFile.text = output;
	}

	/**
	 *
	 */
	private void generateSettingGradle()
	{
		if( !this.getSettingsFile().exists() )
		{
			this.settingFile.parentFile.mkdirs();
			this.settingFile.createNewFile();
		}

		this.settingFile.text = "rootProject.name = '" + project.name + "'";
	}
}
