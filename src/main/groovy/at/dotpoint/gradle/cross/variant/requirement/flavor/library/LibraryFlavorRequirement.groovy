package at.dotpoint.gradle.cross.variant.requirement.flavor.library

import at.dotpoint.gradle.cross.util.DefaultNamed
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement

/**
 * Created by RK on 11.03.16.
 */
class LibraryFlavorRequirement extends DefaultNamed implements IFlavorRequirement
{

	LibraryFlavorRequirement(String name) {
		super(name)
	}

	@Override
	Class<? extends IVariant> getVariantType() {
		return ILibraryFlavor.class;
	}
}
