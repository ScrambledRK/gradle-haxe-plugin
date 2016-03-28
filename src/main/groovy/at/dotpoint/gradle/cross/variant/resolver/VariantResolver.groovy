package at.dotpoint.gradle.cross.variant.resolver

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement

/**
 * Created by RK on 28.03.2016.
 */
abstract class VariantResolver<TVariant extends IVariant, TRequirement extends IVariantRequirement>
		implements IVariantResolver<TVariant, TRequirement>
{

	//
	protected final Set<TVariant> variantContainer;

	//
	public VariantResolver( Set<TVariant> variantContainer )
	{
		this.variantContainer = variantContainer;
	}

	// -------------------------------------------------------- //
	// -------------------------------------------------------- //

	@Override
	abstract public Class<TVariant> getVariantType();

	@Override
	abstract public Class<TRequirement> getRequirementType();

	// -------------------------------------------------------- //
	// -------------------------------------------------------- //

	@Override
	public TVariant resolve(TRequirement requirement)
	{
		TVariant created = this.createVariant( requirement );
		TVariant unique = this.getUniqueVariant( created );

		if ( unique == created )
			this.variantContainer.add( created );

		return unique;
	}

	//
	protected TVariant getUniqueVariant( TVariant compareTarget )
	{
		for ( TVariant variant : this.variantContainer )
		{
			if ( variant.equals( compareTarget ) )
				return variant;
		}

		return compareTarget;
	}

	//
	abstract protected TVariant createVariant(TRequirement requirement);
}