package at.dotpoint.gradle.cross.convention

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.api.Project

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
	public static File getVariationBuildDir( Project project, VariantCombination<IVariant> variant )
	{
		String platform = variant.platform != null 	? variant.platform.displayName 	: "default";
		String flavor 	= variant.flavor != null 	? variant.flavor.displayName 	: "default";

		return new File( project.buildDir, platform + "/" + flavor );
	}
}
