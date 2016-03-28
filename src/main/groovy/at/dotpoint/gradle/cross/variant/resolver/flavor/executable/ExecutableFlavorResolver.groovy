package at.dotpoint.gradle.cross.variant.resolver.flavor.executable

import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.flavor.FlavorResolver

/**
 * Created by RK on 11.03.16.
 */
class ExecutableFlavorResolver extends FlavorResolver<IExecutableFlavor,ExecutableFlavorRequirement> implements IExecutableFlavorResolver
{

	ExecutableFlavorResolver(Set<IExecutableFlavor> variantContainer) {
		super(variantContainer)
	}

	@Override
	Class<IExecutableFlavor> getVariantType() {
		return IExecutableFlavor.class;
	}

	@Override
	Class<ExecutableFlavorRequirement> getRequirementType() {
		return ExecutableFlavorRequirement.class;
	}

}
