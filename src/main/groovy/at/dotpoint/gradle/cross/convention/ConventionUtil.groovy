package at.dotpoint.gradle.cross.convention

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
		String platform = variant.platform != null 	? variant.platform.displayName 	: "default";
		String flavor 	= variant.flavor != null 	? variant.flavor.displayName 	: "default";

		return new File( buildDir, platform + "/" + flavor );
	}
}
