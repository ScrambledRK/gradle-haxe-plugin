package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement

/**
 * Created by RK on 19.03.16.
 */
class ApplicationComponentSpec extends GeneralComponentSpec implements IApplicationComponentSpecInternal
{
	@Override
	List<PlatformRequirement> getTargetPlatforms() {
		return null
	}

	@Override
	List<IFlavorRequirement> getTargetFlavors() {
		return null
	}

	@Override
	void targetPlatform(String targetPlatform) {

	}

	@Override
	void targetFlavor(String targetFlavor) {

	}
}
