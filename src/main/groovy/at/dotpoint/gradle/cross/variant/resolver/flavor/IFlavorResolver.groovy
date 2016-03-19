package at.dotpoint.gradle.cross.variant.resolver.flavor

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolver

/**
 * Created by RK on 11.03.16.
 */
public interface IFlavorResolver<TVariant extends IFlavor, TRequirement extends IFlavorRequirement>
		extends IVariantResolver<TVariant,TRequirement>
{

}