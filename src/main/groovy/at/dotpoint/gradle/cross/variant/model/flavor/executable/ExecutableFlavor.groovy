package at.dotpoint.gradle.cross.variant.model.flavor.executable

import at.dotpoint.gradle.cross.DefaultDisplayNamed
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed

/**
 *
 */
@HasInternalProtocol
//
public interface IExecutableFlavor extends IFlavor {

}

/**
 *
 */
@Managed
//
public interface IExecutableFlavorInternal extends IExecutableFlavor {

}

/**
 * Created by RK on 11.03.16.
 */
class ExecutableFlavor extends DefaultDisplayNamed implements IExecutableFlavorInternal {


	public ExecutableFlavor(String name, String displayName) {
		super(name, displayName)
	}

}

