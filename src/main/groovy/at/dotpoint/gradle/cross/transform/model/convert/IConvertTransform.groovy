package at.dotpoint.gradle.cross.transform.model.convert

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.transform.model.ITaskTransform
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
/**
*  Created by RK on 02.07.2016.
*/
public interface IConvertTransform
		extends ITaskTransform<ISourceSet, VariantCombination<IVariant>>
{

}