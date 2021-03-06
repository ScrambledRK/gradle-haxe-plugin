package at.dotpoint.gradle.cross.variant.container.platform;

import at.dotpoint.gradle.cross.variant.container.VariantContainer;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import org.gradle.internal.reflect.Instantiator;

/**
 * Created by RK on 28.03.2016.
 */
public class PlatformContainer extends VariantContainer<IPlatform> implements IPlatformContainer {

	public PlatformContainer(Instantiator instantiator) {
		super( IPlatform.class, instantiator);
	}
}
