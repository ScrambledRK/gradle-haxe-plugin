package at.dotpoint.gradle.cross.variant.model.flavor

import at.dotpoint.gradle.cross.variant.model.DefaultVariant
/**
 * Created by RK on 11.03.16.
 */
class Flavor extends DefaultVariant implements IFlavorInternal {

	public Flavor( String name) {
		super(name, name)
	}

	public Flavor( String name, String displayName) {
		super(name, displayName)
	}

}
