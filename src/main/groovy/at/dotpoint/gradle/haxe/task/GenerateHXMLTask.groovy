package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.task.ACrossSourceTask
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.haxe.configuration.ConfigurationConstant
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

	//
	private String mainClassPath;

	// ********************************************************************************************** //
	// ********************************************************************************************** //

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
		return configuration;
	}

	public void setConfiguration( IConfiguration configuration )
	{
		this.configuration = configuration;
	}

	/**
	 *
	 * @return
	 */
	String getMainClassPath()
	{
		if( this.mainClassPath == null && this.configuration != null )
		{
			IConfigurationSetting setting = this.configuration.getSettingByName( ConfigurationConstant.KEY_MAIN );

			if( setting != null && setting.value instanceof String )
				this.setMainClassPath( mainClassPath );
		}

		return mainClassPath;
	}

	void setMainClassPath( String mainClassPath )
	{
		this.mainClassPath = mainClassPath;
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

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
	    // classpath:

	    String classpath = "";

        for( String value : this.getClassPaths() )
	        classpath += "\n" + "-cp " + value;

	    // -------------- //
	    // configuration:

	    String configuration = "";

        for( String value : this.getConfigurations() )
	        configuration += "\n" + value;

		// -------------- //
		// total:

		String total = "##";

		total += "\n## generated via gradle task:";
		total += "\n## " + this.name;

		total += "\n\n## classpath:"
		total += classpath;

		total += "\n\n## configurations:"
		total += configuration;

		total += "\n\n## output:"
		total += this.getOutput();

		// -------------- //

		hxmlFile.text = total;
    }

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// classpath:

	/**
	 *
	 * @return
	 */
	private ArrayList<String> getClassPaths()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.sourceSets == null || this.sourceSets.size() == 0 )
			return list;

		// ------------- //

		for( ISourceSet set : this.sourceSets )
		{
			set.source.getSrcDirs().each
			{
				String value = GFileUtils.relativePath( this.project.projectDir, it.absoluteFile );

				if( value != null && !list.contains( value ) )
					list.add( value );
			}
		}

		// ------------- //

		return list;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// configuration:

	/**
	 *
	 * @return
	 */
	private ArrayList<String> getConfigurations()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.configuration == null )
			return list;

		// ------------- //

		for( IConfigurationSetting setting : this.configuration )
		{
			String value = this.getHxmlConfigValue( setting );

			if( value != null && !list.contains( value ) )
				list.add( value );
		}

		// ------------- //

		return list;
	}

	/**
	 *
	 * @param configurationSetting
	 * @return
	 */
	private String getHxmlConfigValue( IConfigurationSetting configurationSetting )
	{
		switch( configurationSetting.name )
		{
			case ConfigurationConstant.KEY_HXML:
				return configurationSetting.value;

			default:
				return null;
		}
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// output:

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
