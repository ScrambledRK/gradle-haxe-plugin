package at.dotpoint.gradle.cross.configuration.requirement.command

import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting

/**
 * Created by RK on 16.05.2016.
 */
class ConfigurationCommand implements IConfigurationCommand
{

	//
	protected ConfigurationCommandType type;

	//
	protected IConfigurationSetting setting;

	// --------------------- //
	// --------------------- //

	/**
	 *
	 */
	public ConfigurationCommand( ConfigurationCommandType type, IConfigurationSetting setting )
	{
		this.type = type;
		this.setting = setting;
	}

	// --------------------- //
	// --------------------- //

	//
	public ConfigurationCommandType getType()
	{
		return this.type;
	}

	//
	public IConfigurationSetting getSetting()
	{
		return this.setting;
	}
}
