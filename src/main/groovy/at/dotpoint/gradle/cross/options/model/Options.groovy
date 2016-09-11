package at.dotpoint.gradle.cross.options.model

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting

/**
 * Created by RK on 16.05.2016.
 */
class Options extends ArrayList<IOptionsSetting> implements IOptions
{
	@Override
	IOptionsSetting getSettingByName( String name )
	{
		for( IOptionsSetting setting : this )
			if( setting.name == name ) return setting;

		return null;
	}
}
