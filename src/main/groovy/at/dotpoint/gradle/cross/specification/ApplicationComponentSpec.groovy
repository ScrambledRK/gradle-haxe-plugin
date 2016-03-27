package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser
import at.dotpoint.gradle.cross.variant.parser.platform.IPlatformNotationParser
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement

/**
 * Created by RK on 19.03.16.
 */
class ApplicationComponentSpec extends GeneralComponentSpec implements IApplicationComponentSpecInternal
{

	 //
    protected final ArrayList<PlatformRequirement> targetPlatformList;
    protected final ArrayList<IFlavorRequirement> targetFlavorList;

	//
	protected final IPlatformNotationParser platformNotationParser;
	protected final IFlavorNotationParser<IFlavorRequirement> flavorNotationParser;

	// --------------------------------------------- //
	// --------------------------------------------- //

	/**
	 *
	 * @param flavorNotationParser
	 * @param targetFlavorList
	 */
	ApplicationComponentSpec( IFlavorNotationParser<IFlavorRequirement> flavorNotationParser )
	{
		this.targetPlatformList = new ArrayList<PlatformRequirement>();
		this.targetFlavorList = new ArrayList<IFlavorRequirement>();

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
	List<IFlavorRequirement> getTargetFlavors()
	{
		return Collections.unmodifiableList( this.targetFlavorList );
	}

	/**
	 *
	 * @return
	 */
	@Override
	List<List<IVariantRequirement>> getVariantRequirements()
	{
		List<List<IVariantRequirement>> variants = new ArrayList<List<IVariantRequirement>>();
		variants.add( this.getTargetPlatforms() );
		variants.add( this.getTargetFlavors() );

		return Collections.unmodifiableList( variants );
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
		IFlavorRequirement requirement = this.flavorNotationParser.parseNotation( flavorRequirements );

		if( requirement != null )
			this.targetFlavorList.add( requirement );
	}
}
