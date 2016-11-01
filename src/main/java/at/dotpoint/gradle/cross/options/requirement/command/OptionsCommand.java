package at.dotpoint.gradle.cross.options.requirement.command;

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;

/**
 * Created by RK on 16.05.2016.
 */
public class OptionsCommand implements IOptionsCommand
{

	//
	protected OptionsCommandType type;

	//
	protected IOptionsSetting setting;

	// --------------------- //
	// --------------------- //

	/**
	 *
	 */
	public OptionsCommand( OptionsCommandType type, IOptionsSetting setting )
	{
		this.type = type;
		this.setting = setting;
	}

	// --------------------- //
	// --------------------- //

	//
	public OptionsCommandType getType()
	{
		return this.type;
	}

	//
	public IOptionsSetting getSetting()
	{
		return this.setting;
	}
}
