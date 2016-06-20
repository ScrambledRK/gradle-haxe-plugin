package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.variant.iterator.VariantContainer
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
	public static String getVariationName( VariantContainer<Named> permutation )
	{
		ArrayList<String> names = new ArrayList<>();

		for( int i = 0; i < permutation.size(); i++ )
		{
			names.add( permutation.get( i ).name );
		}

		return StringUtil.toCamelCase( names );
	}

}
