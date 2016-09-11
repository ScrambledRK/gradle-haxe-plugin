package at.dotpoint.gradle.cross.options.requirement.command

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting

/**
 * Created by RK on 16.05.2016.
 */
interface IOptionsCommand
{
	//
	public OptionsCommandType getType();

	//
	public IOptionsSetting getSetting();
}