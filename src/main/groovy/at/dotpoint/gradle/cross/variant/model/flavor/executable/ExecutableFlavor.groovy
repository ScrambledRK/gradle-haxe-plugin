package at.dotpoint.gradle.cross.variant.model.flavor.executable

import at.dotpoint.gradle.cross.util.DefaultDisplayNamed

/**
 * Created by RK on 11.03.16.
 */
class ExecutableFlavor extends DefaultDisplayNamed implements IExecutableFlavorInternal {


	public ExecutableFlavor(String name) {
		super(name, name)
	}

	public ExecutableFlavor(String name, String displayName) {
		super(name, displayName)
	}

}
