package at.dotpoint.gradle.haxe.task;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.task.ACrossSourceTask;
import at.dotpoint.gradle.haxe.configuration.ConfigurationConstant;
import org.gradle.api.tasks.TaskAction;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.util.GFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 21.05.2016.
 */
public class GenerateHXMLTask extends ACrossSourceTask
{

	//
	private List<ISourceSet> sourceSets;

	//
	private IOptions options;

    //
	private File hxmlFile;

	//
	private String mainClassPath;

	//
	private Set<File> dependencies;

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

		return this.hxmlFile;
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
	public IOptions getOptions()
	{
		return options;
	}

	public void setOptions( IOptions options )
	{
		this.options = options;
	}

	/**
	 *
	 * @return
	 */
	String getMainClassPath()
	{
		if( this.mainClassPath == null && this.options != null )
		{
			IOptionsSetting setting = this.options.getSettingByName( ConfigurationConstant.KEY_MAIN );

			if( setting != null && setting.value instanceof String )
				this.setMainClassPath( mainClassPath );
		}

		return mainClassPath;
	}

	void setMainClassPath( String mainClassPath )
	{
		this.mainClassPath = mainClassPath;
	}

	Set<File> getDependencies()
	{
		return dependencies;
	}

	void setDependencies( Set<File> dependencies )
	{
		this.dependencies = dependencies;
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
	    // dependencies:

	    String dependencies = "";

        for( String value : this.getCompileDependencies() )
	        dependencies += "\n" + value;

	    // -------------- //
	    // options:

	    String options = "";

        for( String value : this.getCompileOptions() )
	        options += "\n" + value;

		// -------------- //
		// total:

		String total = "##";

		total += "\n## generated via gradle task:";
		total += "\n## " + this.name;

		total += "\n\n## classpath:";
		total += classpath;

	    total += "\n\n## dependencies:";
        total += dependencies;

		total += "\n\n## options:";
		total += options;

		total += "\n\n## output:";
		total += this.getOutput();

		// -------------- //

		hxmlFile.text = total;
    }

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// classpath:

	/**
	 */
	private ArrayList<String> getClassPaths()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.sourceSets == null || this.sourceSets.size() == 0 )
			return list;

		// ------------- //

		for( ISourceSet set : this.sourceSets )
		{
			set.getSource().getSrcDirs().each
			{
				String value = GFileUtils.relativePath( this.getProject().getProjectDir(), it.absoluteFile );

				if( value != null && !list.contains( value ) )
					list.add( value );
			}
		}

		// ------------- //

		return list;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// dependencies:

	/**
	 */
	private ArrayList<String> getCompileDependencies()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.dependencies == null )
			return list;

		// ------------- //

		for( File artifact : this.dependencies )
		{
			String value = this.getNativeDependencyOption( artifact );

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
	private String getNativeDependencyOption( File artifact )
	{
		//
		switch( this.targetVariantCombination.platform.name )
		{
			case "java": 	return "-java-lib " + artifact.absolutePath;

			default:
				return null;
		}
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// options:

	/**
	 *
	 * @return
	 */
	private ArrayList<String> getCompileOptions()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.options == null )
			return list;

		// ------------- //

		for( IOptionsSetting setting : this.options )
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
	private String getHxmlConfigValue( IOptionsSetting configurationSetting )
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
		String outputPath = this.getOutputDir(); // new File( this.getOutputDir(), project.name ).path;

		//
		switch( this.targetVariantCombination.platform.name )
		{
			case "java": 	return "\n-java " + outputPath;
			case "flash": 	return "\n-as3 " + outputPath;

			default:
				return "";
		}
	}

}
