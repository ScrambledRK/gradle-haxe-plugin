package at.dotpoint.gradle.cross.variant.resolver.flavor;

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement;
import at.dotpoint.gradle.cross.variant.resolver.VariantResolver;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;

public class FlavorResolver extends VariantResolver<IFlavor, IFlavorRequirement>
		implements IFlavorResolver
{

	FlavorResolver( ExtensiblePolymorphicDomainObjectContainer<IFlavor> variantContainer ) {
		super(variantContainer)
	}

	@Override
	public Class<IFlavor> getVariantType() {
		return IFlavor.class;
	}

	@Override
	public Class<IFlavorRequirement> getRequirementType() {
		return IFlavorRequirement.class;
	}

}