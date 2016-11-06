package at.dotpoint.gradle.cross.variant.resolver.platform;

import at.dotpoint.gradle.cross.variant.container.platform.PlatformContainer;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import at.dotpoint.gradle.cross.variant.resolver.VariantResolver;


/**
 * Created by RK on 11.03.16.
 */
public class PlatformResolver extends VariantResolver<IPlatform,PlatformRequirement> implements IPlatformResolver
{

	//
	public PlatformResolver( PlatformContainer platformContainer ) {
		super( platformContainer );
	}

	@Override
	public Class<IPlatform> getVariantType()
	{
		return IPlatform.class;
	}

	@Override
	public Class<PlatformRequirement> getRequirementType() {
		return PlatformRequirement.class;
	}

}
