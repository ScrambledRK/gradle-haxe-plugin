package at.dotpoint.gradle.haxe.task.java;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

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
			this.outputDir = new File( this.getProject().getBuildDir().getAbsolutePath() );

		return this.outputDir;
	}

	public void setOutputDir( File outputDir )
	{
		this.outputDir = outputDir;
	}

	/**
	 */
	public File getGradleFile()
	{
		if( this.gradleFile == null )
			this.setGradleFile( new File( this.getOutputDir(), "build.gradle" ) );

		return this.gradleFile;
	}

	/**
	 */
	public void setGradleFile( File gradleFile )
	{
		this.gradleFile = gradleFile;
		this.getOutputs().file( this.getGradleFile() );
	}

	/**
	 */
	public File getSettingsFile()
	{
		if( this.settingFile == null )
			this.setSettingsFile( new File( this.getOutputDir(), "settings.gradle" ) );

		return this.settingFile;
	}

	/**
	 */
	public void setSettingsFile( File settingFile )
	{
		this.settingFile = settingFile;
		this.getOutputs().file( this.getSettingsFile() );
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 *
	 */
    @TaskAction
    public void generateGradleProject() throws IOException
    {
		this.generateBuildGradle();
	    this.generateSettingGradle();
    }

	/**
	 *
	 */
	private void generateBuildGradle() throws IOException
	{
		String output = IOUtils.toString( getClass().getResource("/templates/build-java.gradle").openStream() );

		output = output.replaceAll( "%GROUP%", this.getProject().getGroup().toString() );
		output = output.replaceAll( "%VERSION%", this.getProject().getVersion().toString() );

		// -------------- //

		FileUtils.touch( this.gradleFile );
	    FileUtils.writeStringToFile( this.gradleFile,  output );
	}

	/**
	 *
	 */
	private void generateSettingGradle() throws IOException
	{
		FileUtils.touch( this.settingFile );
	    FileUtils.writeStringToFile( this.settingFile,  "rootProject.name = '" + getProject().getName() + "'" );
	}
}
