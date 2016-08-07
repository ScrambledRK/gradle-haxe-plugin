package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.configuration.builder.ConfigurationBuilder
import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.convention.ConventionUtil
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.task.AConvertTask
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils
/**
 * Created by RK on 21.05.2016.
 */
class GenerateHXMLTask extends AConvertTask
{

	private ISourceSet sourceSet;

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
			this.hxmlFile = new File( ConventionUtil.getVariationBuildDir( this.project, this.targetVariantCombination ),
					"convert.hxml" );

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

		String content = "";

		content += "\n## generated via gradle task:";
		content += "\n## " + this.name;

		content += "\n\n## classpaths:"
		content += "\n" + this.getClassPaths();

		content += "\n\n## configurations:"
		content += "\n" + this.getConfigurations();

		// -------------- //

		hxmlFile.text = content;
    }

	/**
	 *
	 * @return
	 */
	private String getClassPaths()
	{
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
		String configs = "";

		IConfiguration configuration = new ConfigurationBuilder( this.project ).build( this.targetVariantCombination );

		configuration.each {
			configs += "\n" + it.name + " " + it.value;
		}

		return configs;
	}

}
