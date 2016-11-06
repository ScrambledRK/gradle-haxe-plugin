package at.dotpoint.gradle.cross.variant.target;

import at.dotpoint.gradle.cross.variant.model.IVariant;

/**
 *
 */
public interface IVariationsSourceInternal extends IVariationsSource
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	void setSourceVariantCombination( VariantCombination<IVariant> combination );
}
