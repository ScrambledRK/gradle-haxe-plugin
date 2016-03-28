package at.dotpoint.gradle.cross.variant.container.flavor

import at.dotpoint.gradle.cross.variant.container.VariantContainer
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.internal.reflect.Instantiator

/**
 * Created by RK on 28.03.2016.
 */
class FlavorContainer extends VariantContainer<IFlavor> implements IFlavorContainer {

	FlavorContainer( Instantiator instantiator )
	{
		super( IFlavor.class, instantiator );
	}
}
