package at.dotpoint.gradle.cross.variant.resolver.buildtype;

import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;
import at.dotpoint.gradle.cross.variant.requirement.buildtype.IBuildTypeRequirement;
import at.dotpoint.gradle.cross.variant.resolver.VariantResolver;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;

public class BuildTypeResolver extends VariantResolver<IBuildType, IBuildTypeRequirement>
		implements IBuildTypeResolver
{

	BuildTypeResolver( ExtensiblePolymorphicDomainObjectContainer<IBuildType> variantContainer ) {
		super(variantContainer);
	}

	@Override
	public Class<IBuildType> getVariantType() {
		return IBuildType.class;
	}

	@Override
	public Class<IBuildTypeRequirement> getRequirementType() {
		return IBuildTypeRequirement.class;
	}

}