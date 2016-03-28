package at.dotpoint.gradle.cross.variant.requirement.platform

import at.dotpoint.gradle.cross.util.DefaultNamed
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement

/**
 * Created by RK on 11.03.16.
 */
class PlatformRequirement extends DefaultNamed implements IVariantRequirement, org.gradle.platform.base.internal.PlatformRequirement
{

	PlatformRequirement(String platformName) {
		super(platformName)
	}

	@Override
	String getPlatformName() {
		return this.getName();
	}

}
