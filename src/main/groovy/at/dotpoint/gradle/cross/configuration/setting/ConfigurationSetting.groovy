package at.dotpoint.gradle.cross.configuration.setting

/**
 * Created by RK on 16.05.2016.
 */
class ConfigurationSetting implements IConfigurationSetting
{

	//
	protected String name;
	protected Object value;

	// --------------------- //
	// --------------------- //

	public ConfigurationSetting()
	{
		this( null, null )
	}

	public ConfigurationSetting( String name, Object value )
	{
		this.name = name;
		this.value = value;
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 *
	 * @return
	 */
	public Object getValue()
	{
		return this.value;
	}
}
