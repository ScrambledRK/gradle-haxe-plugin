package at.dotpoint.gradle.cross.configuration.model

import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting

/**
 * Created by RK on 16.05.2016.
 */
class Configuration extends ArrayList<IConfigurationSetting> implements IConfiguration
{
	@Override
	IConfigurationSetting getSettingByName( String name )
	{
		for( IConfigurationSetting setting : this )
			if( setting.name == name ) return setting;

		return null;
	}
}
