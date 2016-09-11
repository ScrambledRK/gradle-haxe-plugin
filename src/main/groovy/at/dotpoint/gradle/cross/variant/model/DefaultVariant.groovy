package at.dotpoint.gradle.cross.variant.model

import at.dotpoint.gradle.cross.util.DefaultDisplayNamed
import at.dotpoint.gradle.cross.options.requirement.OptionsRequirement
import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement
/**
 * Created by RK on 16.05.2016.
 */
class DefaultVariant extends DefaultDisplayNamed implements IVariant
{
	//
	private IOptionsRequirement configuration;

	// --------------------- //
	// --------------------- //

	public DefaultVariant( String name )
	{
		super( name, name )
	}

	public DefaultVariant( String name, String displayName )
	{
		super( name, displayName )
	}

	// --------------------- //
	// --------------------- //

	/**
	 *
	 * @return
	 */
	IOptionsRequirement getConfiguration()
	{
		if( this.configuration == null )
			this.configuration = this.createConfiguration();

		return this.configuration;
	}

	/**
	 *
	 * @return
	 */
	protected IOptionsRequirement createConfiguration()
	{
		return new OptionsRequirement();
	}

	@Override
	boolean equals( Object obj )
	{
		if( obj instanceof IVariant )
			return this.name.equals( ((IVariant)obj).name );

		return false;
	}
}
