package at.dotpoint.gradle.cross.variant.resolver.platform

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolver
import at.dotpoint.gradle.cross.variant.resolver.VariantResolver
import org.gradle.platform.base.PlatformContainer


/**
 * Created by RK on 11.03.16.
 */
class PlatformResolver extends VariantResolver<Platform,PlatformRequirement> implements IPlatformResolver
{

	//
	public PlatformResolver( PlatformContainer platformContainer ) {
		super( platformContainer );
	}

	@Override
	Class<Platform> getVariantType()
	{
		return IPlatform.class;
	}

	@Override
	Class<PlatformRequirement> getRequirementType() {
		return PlatformRequirement.class;
	}

}
