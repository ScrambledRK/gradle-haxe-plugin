package at.dotpoint.gradle.cross.configuration.model

import at.dotpoint.gradle.cross.configuration.setting.IConfigurationSetting

/**
 * Created by RK on 16.05.2016.
 */
interface IConfiguration extends List<IConfigurationSetting>, RandomAccess, Cloneable
{

}