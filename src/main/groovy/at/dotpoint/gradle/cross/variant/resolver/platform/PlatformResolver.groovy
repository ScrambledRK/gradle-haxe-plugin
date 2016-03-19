package at.dotpoint.gradle.cross.variant.resolver.platform

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolver
import org.gradle.platform.base.Platform
import org.gradle.platform.base.PlatformContainer


/**
 * Created by RK on 11.03.16.
 */
class PlatformResolver implements IPlatformResolver
{

	//
	private final PlatformContainer platformContainer;

	//
	public PlatformResolver( PlatformContainer platformContainer )
	{
		this.platformContainer = platformContainer;
	}

	@Override
	Class<IPlatform> getVariantType()
	{
		return IPlatform.class;
	}

	@Override
	Class<PlatformRequirement> getRequirementType() {
		return PlatformRequirement.class;
	}

	@Override
	IPlatform resolve( PlatformRequirement platformRequirement )
	{
		for (Platform platform : this.platformContainer)
		{
			if ( !(platform instanceof IPlatform) ){
				continue;
			}

			if (platform.name.equals( platformRequirement.platformName )){
				return (IPlatform)platform;
			}
		}

		return new at.dotpoint.gradle.cross.variant.model.platform.Platform( platformRequirement.getPlatformName() );
	}
}
