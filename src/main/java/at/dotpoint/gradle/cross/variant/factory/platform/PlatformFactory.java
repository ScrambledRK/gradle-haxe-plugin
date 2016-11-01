package at.dotpoint.gradle.cross.variant.factory.platform;

import at.dotpoint.gradle.cross.variant.factory.IVariantFactory;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.model.platform.Platform;

/**
 * Created by RK on 28.03.2016.
 */
public class PlatformFactory implements IVariantFactory<IPlatform> {
	@Override
	public IPlatform create(String s) {
		return new Platform( s );
	}
}
