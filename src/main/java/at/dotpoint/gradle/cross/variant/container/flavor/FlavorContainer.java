package at.dotpoint.gradle.cross.variant.container.flavor;

import at.dotpoint.gradle.cross.variant.container.VariantContainer;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import org.gradle.internal.reflect.Instantiator;

/**
 * Created by RK on 28.03.2016.
 */
public class FlavorContainer extends VariantContainer<IFlavor> implements IFlavorContainer {

	public FlavorContainer( Instantiator instantiator )
	{
		super( IFlavor.class, instantiator );
	}
}
