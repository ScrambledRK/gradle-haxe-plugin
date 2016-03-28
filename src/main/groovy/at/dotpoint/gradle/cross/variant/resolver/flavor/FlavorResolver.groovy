package at.dotpoint.gradle.cross.variant.resolver.flavor

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolver
import at.dotpoint.gradle.cross.variant.resolver.VariantResolver

abstract class FlavorResolver<TVariant extends IFlavor, TRequirement extends IFlavorRequirement>
		extends VariantResolver<TVariant, TRequirement>
		implements IFlavorResolver<TVariant, TRequirement>
{

	FlavorResolver(Set<TVariant> variantContainer) {
		super(variantContainer)
	}
}