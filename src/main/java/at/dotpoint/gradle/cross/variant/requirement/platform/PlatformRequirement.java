package at.dotpoint.gradle.cross.variant.requirement.platform;

import at.dotpoint.gradle.cross.util.DefaultNamed;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;

/**
 * Created by RK on 11.03.16.
 */
public class PlatformRequirement extends DefaultNamed implements IVariantRequirement, org.gradle.platform.base.internal.PlatformRequirement
{

	public PlatformRequirement(String platformName) {
		super(platformName);
	}

	@Override
	public String getPlatformName() {
		return this.getName();
	}

	@Override
	public Class<? extends IVariant> getVariantType() {
		return IPlatform.class;
	}
}
