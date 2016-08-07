package at.dotpoint.gradle.cross.configuration.requirement

import at.dotpoint.gradle.cross.configuration.requirement.command.ConfigurationCommand
import at.dotpoint.gradle.cross.configuration.requirement.command.ConfigurationCommandType
import at.dotpoint.gradle.cross.configuration.setting.ConfigurationSetting
import at.dotpoint.gradle.cross.configuration.requirement.command.IConfigurationCommand
import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting

/**
 * Created by RK on 16.05.2016.
 */
class ConfigurationRequirement implements IConfigurationRequirementInternal
{

	//
	protected ArrayList<IConfigurationCommand> commands;

	// --------------------- //
	// --------------------- //

	public ConfigurationRequirement()
	{
		this.commands = new ArrayList<IConfigurationCommand>();
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @return
	 */
	public ArrayList<IConfigurationCommand> getConfigurationCommands()
	{
		return this.commands;
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void add( String key, Object value )
	{
		this.addCommand( ConfigurationCommandType.ADD, key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void set( String key, Object value )
	{
		this.addCommand( ConfigurationCommandType.SET, key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void remove( String key, Object value )
	{
		this.addCommand( ConfigurationCommandType.REMOVE, key, value );
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @param type
	 * @param key
	 * @param value
	 */
	protected void addCommand( ConfigurationCommandType type, String key, Object value )
	{
		IConfigurationSetting setting = this.createConfigurationSetting( key, value );
		IConfigurationCommand command = this.createConfigurationCommand( type, setting );

		this.commands.push( command );
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	protected IConfigurationSetting createConfigurationSetting( String key, Object value )
	{
		return new ConfigurationSetting( key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	protected IConfigurationCommand createConfigurationCommand( ConfigurationCommandType type, IConfigurationSetting setting )
	{
		return new ConfigurationCommand( type, setting );
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @return
	 */
	public String toString()
	{
		return "[Configuration:" + this.commands.toString() + "]";
	}
}
