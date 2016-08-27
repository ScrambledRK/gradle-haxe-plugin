package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.task.ACrossSourceTask
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.tasks.TaskAction
import org.gradle.language.base.LanguageSourceSet
import org.gradle.util.GFileUtils
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends ACrossSourceTask
{

	//
	private List<ISourceSet> sourceSets;

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
			this.setHxmlFile( new File( this.getOutputDir(), "build.hxml" ) );

		return this.hxmlFile
	}

	/**
	 *
	 * @param hxmlFile
	 */
	public void setHxmlFile( File hxmlFile )
	{
		this.hxmlFile = hxmlFile;
		this.outputs.file( this.getHxmlFile() );
	}

	/**
	 *
	 * @param sourceSet
	 */
	public void setSourceSets( List<ISourceSet>  sourceSets )
	{
		this.sourceSets = sourceSets;

		for( LanguageSourceSet set : this.sourceSets )
			this.source( set.source );

		this.inputs.files( this.source );

	}

	public List<ISourceSet> getSourceSets()
	{
		return this.sourceSets;
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
		if( this.sourceSets == null || this.sourceSets.size() == 0 )
			return "";

		// ------------- //

		String cps = "";

		for( ISourceSet set : this.sourceSets )
		{
			set.source.getSrcDirs().each
			{
				cps += "\n" + "-cp " + GFileUtils.relativePath( this.project.projectDir, it.absoluteFile );
			}
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
		String outputPath = new File( this.getOutputDir(), project.name ).path;

		IPlatform platform = this.targetVariantCombination.platform;
		IFlavor flavor = this.targetVariantCombination.flavor;

		//
		if( flavor != null && flavor.name == "library" )
		{
			switch( platform.name )
			{
				case "java": 	return "\n-java " + outputPath;
				case "flash": 	return "\n-as3 " + outputPath;
			}
		}

		return "";
	}

}
