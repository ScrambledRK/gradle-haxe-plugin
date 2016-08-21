package at.dotpoint.gradle.cross.variant.requirement.buildtype

import at.dotpoint.gradle.cross.util.DefaultNamed
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType
/**
 * Created by RK on 11.03.16.
 */
class BuildTypeRequirement extends DefaultNamed implements IBuildTypeRequirement
{

	BuildTypeRequirement( String name) {
		super(name)
	}

	@Override
	Class<? extends IVariant> getVariantType() {
		return IBuildType.class;
	}
}
