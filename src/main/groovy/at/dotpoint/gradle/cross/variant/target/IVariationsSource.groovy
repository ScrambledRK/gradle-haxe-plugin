package at.dotpoint.gradle.cross.variant.target

import at.dotpoint.gradle.cross.variant.model.IVariant

/**
 * Created by RK on 08.07.2016.
 */
interface IVariationsSource
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	VariantCombination<IVariant> getSourceVariantCombination();
}

/**
 *
 */
interface IVariationsSourceInternal extends IVariationsSource
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	void setSourceVariantCombination( VariantCombination<IVariant> combination );
}