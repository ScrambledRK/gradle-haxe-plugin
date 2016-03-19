package at.dotpoint.gradle.cross.variant.resolver

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement

/**
 * Created by RK on 11.03.16.
 */
public interface IVariantResolver<TVariant extends IVariant, TRequirement extends IVariantRequirement>  {

	Class<TVariant> getVariantType();
	Class<TRequirement> getRequirementType();

	TVariant resolve(TRequirement requirement);
}