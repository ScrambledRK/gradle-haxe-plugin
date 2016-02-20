package at.dotpoint.gradle.util

/**
 * Created by RK on 20.02.16.
 */
class StringUtil
{
	static String toCamelCase( String... params )
	{
		if ( params == null || params.length == 0 )
			return null;

		// ----------- //

		String result = null;

		for( String value in params )
		{
			result = result == null ? value : result + value.substring( 0, 1 ).toUpperCase() + value.substring( 1 ).toLowerCase();
		}

		return result;
	}
}
