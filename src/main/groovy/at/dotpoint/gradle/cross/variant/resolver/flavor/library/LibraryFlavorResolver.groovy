package at.dotpoint.gradle.cross.variant.resolver.flavor.library

import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.LibraryFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.flavor.FlavorResolver
import at.dotpoint.gradle.cross.variant.resolver.flavor.executable.IExecutableFlavorResolver

/**
 * Created by RK on 11.03.16.
 */
class LibraryFlavorResolver extends FlavorResolver<ILibraryFlavor,LibraryFlavorRequirement> implements ILibraryFlavorResolver
{

	LibraryFlavorResolver(Set<ILibraryFlavor> variantContainer) {
		super(variantContainer)
	}

	@Override
	Class<ILibraryFlavor> getVariantType() {
		return ILibraryFlavor.class;
	}

	@Override
	Class<LibraryFlavorRequirement> getRequirementType() {
		return LibraryFlavorRequirement.class;
	}

}
