package at.dotpoint.gradle.haxe.task;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;
import at.dotpoint.gradle.cross.task.ASourceTask;
import at.dotpoint.gradle.haxe.configuration.ConfigurationConstant;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RK on 21.05.2016.
 */
public abstract class AHaxeTask extends ASourceTask
{

	//
	private IOptions options;

	//
	private String mainClassPath;

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
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
	 */
	String getMainClassPath()
	{
		if( this.mainClassPath == null && this.options != null )
		{
			IOptionsSetting setting = this.options.getSettingByName( ConfigurationConstant.KEY_MAIN );

			if( setting != null && setting.getValue() instanceof String )
				this.setMainClassPath( (String) setting.getValue() );
		}

		return this.mainClassPath;
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
    public void executeCommand()
    {
	    List<String> options = this.generateCommand();
	    List<String> command = new ArrayList<>();

	    for( String opt : options )
	        command.addAll( Arrays.asList( opt.split( " " ) ) );

	    System.out.println("-----------------------");
	    System.out.println(  command.toString() );
	    System.out.println("-----------------------");

	    // -------------- //

	    this.getProject().exec( it ->
        {
            String haxePath = System.getenv("HAXEPATH");

            if( System.getProperty("os.name").toLowerCase().contains( "win" ) )
                haxePath += "haxe.exe";

            // ----------- //

            it.setExecutable( haxePath );
            it.setArgs( command );
        } );
    }

	//
	protected List<String> generateCommand()
	{
		List<String> total = new ArrayList<>( );

		total.addAll( this.getClassPathCommandOptions() );
		total.addAll( this.getNativeDependencyCommandOptions() );
		total.addAll( this.getHaxeCommandOptions() );
		total.add( this.getOutputCommandOption() );

		if( this.getMainClassPath() != null )
			total.add( "-main " + this.getMainClassPath() );

		return total;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// classpath:

	/**
	 */
	protected ArrayList<String> getClassPathCommandOptions()
	{
		ArrayList<String> list = new ArrayList<>();

		// ------------- //

		for( File dir : this.getSourceSets() )
		{
			String value = "-cp " + dir.getAbsolutePath();

			if( !list.contains( value ) )
				list.add( value );
		}

		// ------------- //

		return list;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// dependencies:

	/**
	 */
	protected ArrayList<String> getNativeDependencyCommandOptions()
	{
		ArrayList<String> list = new ArrayList<>();

		// ------------- //

		for( File artifact : this.getDependencies() )
		{
			String value = this.getNativeDependencyOption( artifact );

			if( value != null && !list.contains( value ) )
				list.add( value );
		}

		// ------------- //

		return list;
	}

	/**
	 */
	abstract protected String getNativeDependencyOption( File artifact );

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// options:

	/**
	 */
	private ArrayList<String> getHaxeCommandOptions()
	{
		ArrayList<String> list = new ArrayList<>();

		if( this.options == null )
			return list;

		// ------------- //

		for( IOptionsSetting setting : this.options )
		{
			String value = this.getHaxeConfigurationValue( setting );

			if( value != null && !list.contains( value ) )
				list.add( value );
		}

		// ------------- //

		return list;
	}

	/**
	 */
	private String getHaxeConfigurationValue( IOptionsSetting configurationSetting )
	{
		switch( configurationSetting.getName() )
		{
			case ConfigurationConstant.KEY_HXML:
				return (String)configurationSetting.getValue();

			case ConfigurationConstant.KEY_MACRO:
				return "--macro \"" + configurationSetting.getValue().toString().replace( '"', '\'' ) + "\"";

			default:
				return null;
		}
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //
	// output:

	/**
	 */
	abstract protected String getOutputCommandOption();

}
