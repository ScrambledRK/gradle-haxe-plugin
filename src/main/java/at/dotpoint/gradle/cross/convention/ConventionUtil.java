package at.dotpoint.gradle.cross.convention;

import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;

import java.io.File;

/**
 * Created by RK on 2016-08-07.
 */
public class ConventionUtil
{
	/**
	 */
	public static File getVariationBuildDir( File buildDir, VariantCombination<IVariant> variant )
	{
		return new File( buildDir, NameUtil.getVariationName( variant ) );
	}
}
