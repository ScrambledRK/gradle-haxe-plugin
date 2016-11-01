package at.dotpoint.gradle.cross.options.model;

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by RK on 16.05.2016.
 */
public interface IOptions extends List<IOptionsSetting>, RandomAccess, Cloneable
{
	IOptionsSetting getSettingByName( String name );
}