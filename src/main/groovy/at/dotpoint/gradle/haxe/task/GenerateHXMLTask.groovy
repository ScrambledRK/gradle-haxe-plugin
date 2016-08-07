package at.dotpoint.gradle.haxe.task

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
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
			this.hxmlFile = new File( this.project.getBuildDir(), this.getHxmlFileName() );

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

	/**
	 *
	 * @return
	 */
	private String getHxmlFileName()
	{
		String platform = this.targetVariantCombination.platform.displayName;
		String flavor = "default";

		if(  this.targetVariantCombination.flavor != null )
			flavor = this.targetVariantCombination.flavor.displayName;

		return  platform + "/" + flavor + "/" +	"convert.hxml"
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

		IConfiguration configuration = this.targetVariantCombination.getConfiguration();

		configuration.each {
			configs += "\n" + it.name + " " + it.value;
		}

		return configs;
	}

}
