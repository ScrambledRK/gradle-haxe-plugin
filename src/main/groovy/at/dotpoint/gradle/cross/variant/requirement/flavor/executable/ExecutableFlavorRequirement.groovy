package at.dotpoint.gradle.cross.variant.requirement.flavor.executable

import at.dotpoint.gradle.cross.util.DefaultNamed
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement

/**
 * Created by RK on 11.03.16.
 */
class ExecutableFlavorRequirement extends DefaultNamed implements IFlavorRequirement
{

	ExecutableFlavorRequirement(String name) {
		super(name)
	}

	@Override
	Class<? extends IVariant> getVariantType() {
		return IExecutableFlavor.class;
	}
}
