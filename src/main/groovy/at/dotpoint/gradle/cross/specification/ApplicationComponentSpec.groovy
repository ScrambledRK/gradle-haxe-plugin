package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser
import at.dotpoint.gradle.cross.variant.parser.platform.IPlatformNotationParser
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement

/**
 * Created by RK on 19.03.16.
 */
class ApplicationComponentSpec<TFlavorRequirement extends IFlavorRequirement>
		extends GeneralComponentSpec implements IApplicationComponentSpecInternal<TFlavorRequirement>
{

	 //
    protected final ArrayList<PlatformRequirement> targetPlatformList;
    protected final ArrayList<TFlavorRequirement> targetFlavorList;

	//
	protected final IPlatformNotationParser platformNotationParser;
	protected final IFlavorNotationParser<TFlavorRequirement> flavorNotationParser;

	// --------------------------------------------- //
	// --------------------------------------------- //

	/**
	 *
	 * @param flavorNotationParser
	 * @param targetFlavorList
	 */
	ApplicationComponentSpec( IFlavorNotationParser<TFlavorRequirement> flavorNotationParser )
	{
		this.targetPlatformList = new ArrayList<PlatformRequirement>();
		this.targetFlavorList = new ArrayList<TFlavorRequirement>();

		this.platformNotationParser = PlatformNotationParser.getInstance();
		this.flavorNotationParser = flavorNotationParser;
	}

	// --------------------------------------------- //
	// --------------------------------------------- //

	/**
	 *
	 * @return
	 */
	@Override
	List<PlatformRequirement> getTargetPlatforms()
	{
		return Collections.unmodifiableList( this.targetPlatformList );
	}

	/**
	 *
	 * @return
	 */
	@Override
	List<TFlavorRequirement> getTargetFlavors()
	{
		return Collections.unmodifiableList( this.targetFlavorList );
	}

	/**
	 *
	 * @param platformRequirements
	 */
	@Override
	public void platform( Object platformRequirements )
	{
		PlatformRequirement requirement = this.platformNotationParser.parseNotation( platformRequirements );

		if( requirement != null )
			this.targetPlatformList.add( requirement );
	}

	/**
	 *
	 * @param flavorRequirements
	 */
	@Override
	public void flavor( Object flavorRequirements )
	{
		TFlavorRequirement requirement = this.flavorNotationParser.parseNotation( flavorRequirements );

		if( requirement != null )
			this.targetFlavorList.add( requirement );
	}
}
