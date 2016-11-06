package at.dotpoint.gradle.cross.variant.resolver;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;

/**
 * Created by RK on 28.03.2016.
 */
public abstract class VariantResolver<TVariant extends IVariant, TRequirement extends IVariantRequirement>
		implements IVariantResolver<TVariant, TRequirement>
{

	//
	protected final ExtensiblePolymorphicDomainObjectContainer<TVariant> variantContainer;

	//
	public VariantResolver( ExtensiblePolymorphicDomainObjectContainer<TVariant> variantContainer )
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
		TVariant existing = this.getVariantFromContainer( requirement );

		if ( existing == null )
			return this.createVariant( requirement );

		return existing;
	}

	//
	protected TVariant getVariantFromContainer( TRequirement requirement )
	{
		for ( TVariant variant : this.variantContainer )
		{
			if ( variant.getName().equals( requirement.getName() ) )
				return variant;
		}

		return null;
	}

	//
	protected TVariant createVariant( TRequirement requirement )
	{
		return this.variantContainer.create( requirement.getName(), this.getVariantType() );
	}
}