package at.dotpoint.gradle.cross.variant.resolver.flavor.executable

import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement

/**
 * Created by RK on 11.03.16.
 */
class ExecutableFlavorResolver implements IExecutableFlavorResolver
{
	@Override
	Class<IExecutableFlavor> getVariantType() {
		return IExecutableFlavor.class;
	}

	@Override
	Class<ExecutableFlavorRequirement> getRequirementType() {
		return ExecutableFlavorRequirement.class;
	}

	@Override
	IExecutableFlavor resolve( ExecutableFlavorRequirement requirement ) {
		return new ExecutableFlavor( requirement.name );
	}
}
