package at.dotpoint.gradle.cross.options.requirement

import at.dotpoint.gradle.cross.options.requirement.command.OptionsCommand
import at.dotpoint.gradle.cross.options.requirement.command.OptionsCommandType
import at.dotpoint.gradle.cross.options.setting.OptionsSetting
import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand
import at.dotpoint.gradle.cross.options.setting.IOptionsSetting

/**
 * Created by RK on 16.05.2016.
 */
class OptionsRequirement implements IOptionsRequirementInternal
{

	//
	protected ArrayList<IOptionsCommand> commands;

	// --------------------- //
	// --------------------- //

	public OptionsRequirement()
	{
		this.commands = new ArrayList<IOptionsCommand>();
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @return
	 */
	public ArrayList<IOptionsCommand> getConfigurationCommands()
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
		this.addCommand( OptionsCommandType.ADD, key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void set( String key, Object value )
	{
		this.addCommand( OptionsCommandType.SET, key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void remove( String key, Object value )
	{
		this.addCommand( OptionsCommandType.REMOVE, key, value );
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @param type
	 * @param key
	 * @param value
	 */
	protected void addCommand( OptionsCommandType type, String key, Object value )
	{
		IOptionsSetting setting = this.createConfigurationSetting( key, value );
		IOptionsCommand command = this.createConfigurationCommand( type, setting );

		this.commands.push( command );
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	protected IOptionsSetting createConfigurationSetting( String key, Object value )
	{
		return new OptionsSetting( key, value );
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	protected IOptionsCommand createConfigurationCommand( OptionsCommandType type, IOptionsSetting setting )
	{
		return new OptionsCommand( type, setting );
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
