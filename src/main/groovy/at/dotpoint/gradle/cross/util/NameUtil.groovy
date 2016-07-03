package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.variant.iterator.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.api.Named

/**
 * Created by gehat on 16.06.16.
 */
class NameUtil
{
	/**
	 *
	 * @param permutation
	 * @return
	 */
	public static String getVariationName( VariantCombination<Named> permutation )
	{
		ArrayList<String> names = new ArrayList<>();

		for( int i = 0; i < permutation.size(); i++ )
			names.add( permutation.get( i ).name );

		return StringUtil.toCamelCase( names );
	}

	/**
	 *
	 * @param prefix verb describing the tasks action
	 * @param sourceSet SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @return unique name for the task
	 */
	public static String generateTransformTaskName( String prefix, ISourceSet sourceSet, VariantCombination<IVariant> targetVariation )
	{
		return StringUtil.toCamelCase( prefix, getVariationName( targetVariation ), sourceSet.name );
	}
}
