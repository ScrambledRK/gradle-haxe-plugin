package at.dotpoint.gradle.cross.transform.convert

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.transform.ATaskTransform
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination

/**
*  Created by RK on 02.07.2016.
*/
abstract class AConvertTransform extends ATaskTransform<ISourceSet, VariantCombination<IVariant>> implements IConvertTransform
{

}