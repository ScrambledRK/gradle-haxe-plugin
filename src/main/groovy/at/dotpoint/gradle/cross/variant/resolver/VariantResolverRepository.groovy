package at.dotpoint.gradle.cross.variant.resolver

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import com.google.common.collect.Lists
import org.gradle.api.InvalidUserDataException



/**
 *
 */
public class VariantResolverRepository implements IVariantResolverRepository
{

	//
	private final List<IVariantResolver<? extends IVariant, ? extends IVariantRequirement>> variantResolvers =
			new ArrayList<IVariantResolver<? extends IVariant, ? extends IVariantRequirement>> ();

	@Override
	void register( IVariantResolver<? extends IVariant, ? extends IVariantRequirement> variantResolver )
	{
		this.variantResolvers.add( variantResolver );
	}

	@Override
	public <TVariant extends IVariant, TRequirement extends IVariantRequirement> TVariant resolve(
			Class<TVariant> variantType, TRequirement variantRequirement )
	{
		for( IVariantResolver<?,?> variantResolver : this.variantResolvers )
		{
			if( variantResolver.getVariantType().equals(variantType) && variantResolver.getRequirementType() == variantRequirement.class )
			{
				@SuppressWarnings("unchecked")
				  IVariantResolver<TVariant,TRequirement> vr = (IVariantResolver<TVariant,TRequirement>) variantResolver;

				TVariant resolved = vr.resolve( variantRequirement );

				if (resolved != null) {
					return resolved;
				}
			}
		}

		throw new InvalidUserDataException( String.format("Invalid %s: %s", variantType.getSimpleName(), variantRequirement) );
	}
}