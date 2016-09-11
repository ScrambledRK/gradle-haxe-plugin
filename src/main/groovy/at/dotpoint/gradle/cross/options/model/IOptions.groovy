package at.dotpoint.gradle.cross.options.model

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting

/**
 * Created by RK on 16.05.2016.
 */
interface IOptions extends List<IOptionsSetting>, RandomAccess, Cloneable
{
	public IOptionsSetting getSettingByName( String name );
}