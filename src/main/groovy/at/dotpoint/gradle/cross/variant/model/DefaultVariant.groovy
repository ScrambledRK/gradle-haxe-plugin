package at.dotpoint.gradle.cross.variant.model

import at.dotpoint.gradle.cross.util.DefaultDisplayNamed
import at.dotpoint.gradle.cross.configuration.requirement.ConfigurationRequirement
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirement
/**
 * Created by RK on 16.05.2016.
 */
class DefaultVariant extends DefaultDisplayNamed implements IVariant
{
	//
	private IConfigurationRequirement configuration;

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
	IConfigurationRequirement getConfiguration()
	{
		if( this.configuration == null )
			this.configuration = this.createConfiguration();

		return this.configuration;
	}

	/**
	 *
	 * @return
	 */
	protected IConfigurationRequirement createConfiguration()
	{
		return new ConfigurationRequirement();
	}

	@Override
	boolean equals( Object obj )
	{
		if( obj instanceof IVariant )
			return this.name.equals( ((IVariant)obj).name );

		return false;
	}
}
