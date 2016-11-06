package at.dotpoint.gradle.cross.variant.target;

import at.dotpoint.gradle.cross.variant.model.IVariant;

/**
 *
 */
public interface IVariationsTargetInternal extends IVariationsTarget
{
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	void setTargetVariantCombination( VariantCombination<IVariant> combination );
}
