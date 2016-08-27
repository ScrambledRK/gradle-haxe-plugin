package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
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
	 * @param prefix
	 * @param namedObject
	 * @param targetVariation
	 * @return
	 */
	public static String generateTransformTaskName( String prefix, Named namedObject, VariantCombination<IVariant> targetVariation )
	{
		return StringUtil.toCamelCase( prefix, getVariationName( targetVariation ), namedObject.name );
	}

	/**
	 *
	 * @param prefix
	 * @param targetVariation
	 * @return
	 */
	public static String generateTransformTaskName( String prefix, VariantCombination<IVariant> targetVariation )
	{
		return StringUtil.toCamelCase( prefix, getVariationName( targetVariation ) );
	}

	/**
	 *
	 * @param prefix
	 * @param namedObject
	 * @return
	 */
	public static String generateTransformTaskName( String prefix, Named namedObject )
	{
		return StringUtil.toCamelCase( prefix, namedObject.name );
	}
}
