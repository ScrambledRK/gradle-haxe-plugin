package at.dotpoint.gradle.cross.variant.model.flavor.library

import at.dotpoint.gradle.cross.variant.model.DefaultVariant

/**
 * Created by RK on 11.03.16.
 */
class LibraryFlavor extends DefaultVariant implements ILibraryFlavorInternal {

	public LibraryFlavor(String name) {
		super(name, name)
	}

	public LibraryFlavor(String name, String displayName) {
		super(name, displayName)
	}

}
