package at.dotpoint.gradle.cross.options.requirement.command;

import at.dotpoint.gradle.cross.options.setting.IOptionsSetting;

/**
 * Created by RK on 16.05.2016.
 */
public class OptionsCommand implements IOptionsCommand
{

	//
	protected OptionsCommandType type;

	//
	protected IOptionsSetting setting;

	// --------------------- //
	// --------------------- //

	/**
	 *
	 */
	public OptionsCommand( OptionsCommandType type, IOptionsSetting setting )
	{
		this.type = type;
		this.setting = setting;
	}

	// --------------------- //
	// --------------------- //

	//
	public OptionsCommandType getType()
	{
		return this.type;
	}

	//
	public IOptionsSetting getSetting()
	{
		return this.setting;
	}
	
	// --------------------- //
	// --------------------- //
	
	
	@Override
	public String toString()
	{
		final StringBuffer sb = new StringBuffer( "OptionsCommand{" );
		sb.append( "type=" ).append( type );
		sb.append( ", setting=" ).append( setting );
		sb.append( '}' );
		return sb.toString();
	}
}
