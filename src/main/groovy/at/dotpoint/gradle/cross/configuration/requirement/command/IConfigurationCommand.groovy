package at.dotpoint.gradle.cross.configuration.requirement.command

import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting

/**
 * Created by RK on 16.05.2016.
 */
interface IConfigurationCommand
{
	//
	public ConfigurationCommandType getType();

	//
	public IConfigurationSetting getSetting();
}