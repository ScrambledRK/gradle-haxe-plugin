package at.dotpoint.gradle.cross.variant.resolver.flavor.library

import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.LibraryFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.flavor.executable.IExecutableFlavorResolver

/**
 * Created by RK on 11.03.16.
 */
class LibraryFlavorResolver implements ILibraryFlavorResolver
{

	@Override
	Class<ILibraryFlavor> getVariantType() {
		return ILibraryFlavor.class;
	}

	@Override
	Class<LibraryFlavorRequirement> getRequirementType() {
		return LibraryFlavorRequirement.class;
	}

	@Override
	ILibraryFlavor resolve(LibraryFlavorRequirement requirement) {
		return new LibraryFlavor( requirement.name );
	}
}
