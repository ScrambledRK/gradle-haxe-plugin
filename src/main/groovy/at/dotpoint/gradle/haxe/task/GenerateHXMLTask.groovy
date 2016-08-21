package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.task.AConvertTask
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends AConvertTask
{

	//
	private ISourceSet sourceSet;

	//
	private IConfiguration configuration

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
		if( this.hxmlFile == null )
			this.hxmlFile = new File( this.getOutputDir(), "convert.hxml" );

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

	/**
	 *
	 * @param sourceSet
	 */
	public void setSourceSet( ISourceSet sourceSet )
	{
		this.sourceSet = sourceSet;
		this.source = sourceSet.source;

		this.inputs.files( this.source );
		this.outputs.file( this.getHxmlFile() );
	}

	public ISourceSet getSourceSet()
	{
		return sourceSet
	}

	/**
	 *
	 * @return
	 */
	public IConfiguration getConfiguration()
	{
		return configuration
	}

	public void setConfiguration( IConfiguration configuration )
	{
		this.configuration = configuration
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 *
	 */
    @TaskAction
    public void generateHXML()
    {
		if( !hxmlFile.exists() )
		{
			hxmlFile.parentFile.mkdirs();
			hxmlFile.createNewFile();
		}

		// -------------- //

		String content = "##";

		content += "\n## generated via gradle task:";
		content += "\n## " + this.name;

		content += "\n\n## classpaths:"
		content += this.getClassPaths();

		content += "\n\n## configurations:"
		content += this.getConfigurations();

		content += "\n\n## output:"
		content += this.getOutput();

		// -------------- //

		hxmlFile.text = content;
    }

	/**
	 *
	 * @return
	 */
	private String getClassPaths()
	{
		if( this.sourceSet == null )
			return "";

		// ------------- //

		String cps = "";

		this.sourceSet.source.getSrcDirs().each
		{
			cps += "\n" + "-cp " + GFileUtils.relativePath( this.project.projectDir, it.absoluteFile );
		}

		return cps;
	}

	/**
	 *
	 * @return
	 */
	private String getConfigurations()
	{
		if( this.configuration == null )
			return "";

		// ------------- //

		String configs = "";

		this.configuration.each
		{
			configs += "\n" + this.getHxmlConfigValue( it );
		}

		return configs;
	}

	/**
	 *
	 * @param configurationSetting
	 * @return
	 */
	private String getHxmlConfigValue( IConfigurationSetting configurationSetting )
	{
		if( configurationSetting.name == "hxml" )
			return configurationSetting.value;

		return "";
	}

	/**
	 *
	 * @return
	 */
	private String getOutput()
	{
		String outputPath = new File( this.getOutputDir(), project.rootProject.name ).path;

		IPlatform platform = this.targetVariantCombination.platform;
		IFlavor flavor = this.targetVariantCombination.flavor;

		//
		if( flavor != null && flavor.name == "library" )
		{
			switch( platform.name )
			{
				case "java": 	return "-java " + outputPath;
				case "flash": 	return "-as3 " + outputPath;
			}
		}

		return "";
	}

}
