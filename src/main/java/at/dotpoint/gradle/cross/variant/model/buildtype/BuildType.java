package at.dotpoint.gradle.cross.variant.model.buildtype;

import at.dotpoint.gradle.cross.variant.model.DefaultVariant;
/**
 * Created by RK on 11.03.16.
 */
public class BuildType extends DefaultVariant implements IBuildTypeInternal {

	public BuildType( String name) {
		super(name, name);
	}

	public BuildType( String name, String displayName) {
		super(name, displayName);
	}
}
