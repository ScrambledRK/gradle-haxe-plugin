package at.dotpoint.gradle.cross.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by RK on 20.02.16.
 */
public class StringUtil
{
	public static String toCamelCase( List<String> params )
	{
		if ( params == null || params.size() == 0 )
			return null;

		// ----------- //

		String result = null;

		for( String value : params )
		{
			result = result == null ? value : result + value.substring( 0, 1 ).toUpperCase() + value.substring( 1 );
		}

		return result;
	}

	public static String toCamelCase( String... params )
	{
		return toCamelCase( Arrays.asList( params ) );
	}
}
