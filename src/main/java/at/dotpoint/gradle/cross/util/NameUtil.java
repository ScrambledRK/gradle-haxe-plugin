package at.dotpoint.gradle.cross.util;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	public static String getBinaryTaskName( IApplicationBinarySpec binarySpec, String prefix  )
	{
		return binarySpec.getTasks().taskName( prefix );
	}

	public static String getBinaryTaskName( IApplicationBinarySpec binarySpec, String prefix, String... postFix  )
	{
		List<String> names = new ArrayList<>();
			names.add( binarySpec.getTasks().taskName( prefix ) );
			names.addAll( Arrays.asList( postFix ) );

		return StringUtil.toCamelCase( names );
	}
}
