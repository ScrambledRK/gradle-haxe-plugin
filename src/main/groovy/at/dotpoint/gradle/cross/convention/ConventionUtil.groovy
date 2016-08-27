package at.dotpoint.gradle.cross.convention

import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
/**
 * Created by RK on 2016-08-07.
 */
class ConventionUtil
{
	/**
	 *
	 * @param variant
	 * @return
	 */
	public static File getVariationBuildDir( File buildDir, VariantCombination<IVariant> variant )
	{
		return new File( buildDir, NameUtil.getVariationName( variant ) );
	}
}
