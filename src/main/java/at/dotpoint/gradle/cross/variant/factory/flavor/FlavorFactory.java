package at.dotpoint.gradle.cross.variant.factory.flavor;

import at.dotpoint.gradle.cross.variant.factory.IVariantFactory;
import at.dotpoint.gradle.cross.variant.model.flavor.Flavor;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
/**
 * Created by RK on 28.03.2016.
 */
public class FlavorFactory implements IVariantFactory<IFlavor>
{
	@Override
	public IFlavor create(String s) {
		return new Flavor( s );
	}
}
