package at.dotpoint.gradle.cross.variant.requirement.flavor;

import at.dotpoint.gradle.cross.util.DefaultNamed;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
/**
 * Created by RK on 11.03.16.
 */
public class FlavorRequirement extends DefaultNamed implements IFlavorRequirement
{

	public FlavorRequirement( String name) {
		super(name);
	}

	@Override
	public Class<? extends IVariant> getVariantType() {
		return IFlavor.class;
	}
}
