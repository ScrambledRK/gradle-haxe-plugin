package at.dotpoint.gradle.cross.variant.target

import at.dotpoint.gradle.cross.variant.model.IVariant

/**
 * Created by RK on 08.07.2016.
 */
interface IVariationsTarget
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	VariantCombination<IVariant> getTargetVariantCombination();
}

/**
 *
 */
interface IVariationsTargetInternal extends IVariationsTarget
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	void setTargetVariantCombination( VariantCombination<IVariant> combination );
}