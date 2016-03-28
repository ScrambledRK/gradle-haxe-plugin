package at.dotpoint.gradle.cross.variant.factory.flavor

import at.dotpoint.gradle.cross.variant.factory.IVariantFactory
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.LibraryFlavor

/**
 * Created by RK on 28.03.2016.
 */
class LibraryFlavorFactory implements IVariantFactory<ILibraryFlavor> {
	@Override
	ILibraryFlavor create(String s) {
		return new LibraryFlavor( s );
	}
}
