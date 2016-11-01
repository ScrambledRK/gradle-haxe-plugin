package at.dotpoint.gradle.cross.util;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.Named;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by gehat on 16.06.16.
 */
public class NameUtil
{
	/**
	 */
	public static String getVariationName( VariantCombination<? extends Named> permutation )
	{
		ArrayList<String> names = permutation.stream()
				.map( Named::getName )
				.collect( Collectors.toCollection( ArrayList::new ) );

		return StringUtil.toCamelCase( names );
	}

	/**
	 */
	public static String generateTransformTaskName( String prefix, Named namedObject, VariantCombination<IVariant> targetVariation )
	{
		return StringUtil.toCamelCase( prefix, getVariationName( targetVariation ), namedObject.getName() );
	}

	/**
	 */
	public static String generateTransformTaskName( String prefix, VariantCombination<IVariant> targetVariation )
	{
		return StringUtil.toCamelCase( prefix, getVariationName( targetVariation ) );
	}

	/**

	 */
	public static String generateTransformTaskName( String prefix, Named namedObject )
	{
		return StringUtil.toCamelCase( prefix, namedObject.getName() );
	}
}
