package at.dotpoint.gradle.cross.variant.model.platform;

import at.dotpoint.gradle.cross.variant.model.DefaultVariant;

/**
 * Created by RK on 11.03.16.
 */
public class Platform extends DefaultVariant implements IPlatformInternal {


	public Platform(String name) {
		super(name, name);
	}

	public Platform(String name, String displayName) {
		super(name, displayName);
	}
}
