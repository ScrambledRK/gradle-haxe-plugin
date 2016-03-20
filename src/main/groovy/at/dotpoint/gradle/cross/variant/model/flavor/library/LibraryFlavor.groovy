package at.dotpoint.gradle.cross.variant.model.flavor.library

import at.dotpoint.gradle.cross.DefaultDisplayNamed
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed

/**
 *
 */
@HasInternalProtocol
//
public interface ILibraryFlavor extends IFlavor {

}

/**
 *
 */
@Managed
//
public interface ILibraryFlavorInternal extends ILibraryFlavor {

}

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

