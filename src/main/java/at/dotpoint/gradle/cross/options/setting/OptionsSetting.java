package at.dotpoint.gradle.cross.options.setting;

/**
 * Created by RK on 16.05.2016.
 */
public class OptionsSetting implements IOptionsSetting
{

	//
	protected String name;
	protected Object value;

	// --------------------- //
	// --------------------- //

	public OptionsSetting()
	{
		this( null, null );
	}

	public OptionsSetting( String name, Object value )
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
	
	// --------------------- //
	// --------------------- //
	
	
	@Override
	public String toString()
	{
		final StringBuffer sb = new StringBuffer( "OptionsSetting{" );
		sb.append( "name='" ).append( name ).append( '\'' );
		sb.append( ", value=" ).append( value );
		sb.append( '}' );
		return sb.toString();
	}
}
