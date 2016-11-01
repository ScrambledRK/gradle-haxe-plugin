package at.dotpoint.gradle.cross.variant.resolver;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;

/**
 *
 */
public interface IVariantResolverRepository {
	//
	void register( IVariantResolver<? extends IVariant, ? extends IVariantRequirement> variantResolver );

	//
	<TVariant extends IVariant, TRequirement extends IVariantRequirement> TVariant resolve(
			Class<TVariant> variantType, TRequirement variantRequirement );
}
