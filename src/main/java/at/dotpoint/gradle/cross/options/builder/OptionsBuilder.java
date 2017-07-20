package at.dotpoint.gradle.cross.options.builder;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.model.Options;
import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirementInternal;
import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand;
import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;
import at.dotpoint.gradle.cross.options.setting.OptionsSetting;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by RK on 16.05.2016.
 */
public class OptionsBuilder implements IOptionsBuilder
{
	//
	private static final Logger LOGGER = Logging.getLogger(OptionsBuilder.class);
	
	//
	private final File buildDir;

	/**
	 */
	public OptionsBuilder( File buildDir )
	{
		this.buildDir = buildDir;
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 */
	public IOptions build( Iterable<IOptionsRequirementInternal> requirements )
	{
		IOptions configuration = new Options();
		ArrayList<IOptionsCommand> commands = new ArrayList<>();
		
		//
		for( IOptionsRequirementInternal requirement : requirements )
			commands.addAll( requirement.getConfigurationCommands() );
		
		//
		commands.sort( Comparator.comparing( IOptionsCommand::getType ) );
		
		for( IOptionsCommand command : commands )
		{
			switch( command.getType() )
			{
				case ADD:
					this.addConfiguration( command, configuration );
					break;
					
				case REMOVE:
					this.removeConfiguration( command, configuration );
					break;
					
				case SET:
				{
					this.removeConfiguration( command, configuration );
					this.addConfiguration( command, configuration );
					break;
				}
			}
			
		}

		return configuration;
	}

	//
	private void addConfiguration( IOptionsCommand command, IOptions configuration )
	{
		String name = command.getSetting().getName();
		Object value = command.getSetting().getValue();
		
		configuration.add( new OptionsSetting( name, value ) );
	}
	
	//
	private void removeConfiguration( IOptionsCommand command, IOptions configuration )
	{
		IOptionsSetting setting = this.getConfiguration( command, configuration );
		
		while( setting != null )
		{
			LOGGER.info( "removing: " + setting );
			
			configuration.remove( setting );
			setting = this.getConfiguration( command, configuration );
		}
	}
	
	//
	private IOptionsSetting getConfiguration( IOptionsCommand command, IOptions configuration )
	{
		return configuration.getSettingByName( command.getSetting().getName() );
	}
}
