package at.dotpoint.gradle.cross.options.model;

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;

import java.util.ArrayList;

/**
 * Created by RK on 16.05.2016.
 */
public class Options extends ArrayList<IOptionsSetting> implements IOptions
{
	@Override
	public IOptionsSetting getSettingByName( String name )
	{
		for( IOptionsSetting setting : this )
			if( setting.name == name ) return setting;

		return null;
	}
}
