package at.dotpoint.gradle.cross.variant.model.flavor.library

import at.dotpoint.gradle.cross.util.DefaultDisplayNamed

/**
 * Created by RK on 11.03.16.
 */
class LibraryFlavor extends DefaultDisplayNamed implements ILibraryFlavorInternal {

	public LibraryFlavor(String name) {
		super(name, name)
	}

	public LibraryFlavor(String name, String displayName) {
		super(name, displayName)
	}

}
