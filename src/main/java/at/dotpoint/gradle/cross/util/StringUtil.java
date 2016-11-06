package at.dotpoint.gradle.cross.util;

import java.util.ArrayList;

/**
 * Created by RK on 20.02.16.
 */
public class StringUtil
{
	public static String toCamelCase( ArrayList<String> params )
	{
		return toCamelCase( (String[]) params.toArray() );
	}

	public static String toCamelCase( String... params )
	{
		if ( params == null || params.length == 0 )
			return null;

		// ----------- //

		String result = null;

		for( String value : params )
		{
			result = result == null ? value : result + value.substring( 0, 1 ).toUpperCase() + value.substring( 1 );
		}

		return result;
	}
}
