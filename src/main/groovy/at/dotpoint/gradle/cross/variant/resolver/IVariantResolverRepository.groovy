package at.dotpoint.gradle.cross.variant.resolver

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement

/**
 *
 */
public interface IVariantResolverRepository {
	//
	public void register(IVariantResolver<? extends IVariant, ? extends IVariantRequirement> variantResolver);

	//
	public <TVariant extends IVariant, TRequirement extends IVariantRequirement> TVariant resolve(
			Class<TVariant> variantType, TRequirement variantRequirement);
}
