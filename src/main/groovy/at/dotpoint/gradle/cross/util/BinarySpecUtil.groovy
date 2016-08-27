package at.dotpoint.gradle.cross.util

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import org.gradle.language.base.LanguageSourceSet
/**
 * Created by RK on 2016-08-27.
 */
class BinarySpecUtil
{
	/**
	 *
	 * @param binarySpec
	 */
	public static ArrayList<ISourceSet> getSourceSetList( IApplicationBinarySpec binarySpec )
	{
		ArrayList<ISourceSet> sourceSets = new ArrayList<>();

		populateInputSourceSets( binarySpec.sources.iterator(), sourceSets );
		populateInputSourceSets( binarySpec.application.sources.iterator(), sourceSets );

		return sourceSets;
	}

	/**
	 *
	 * @param sourceSets
	 * @param input
	 */
	private static void populateInputSourceSets( Iterator<LanguageSourceSet> input,
	                                             ArrayList<ISourceSet> sourceSets )
	{
		while( input.hasNext() )
		{
			LanguageSourceSet languageSourceSet = input.next();

			if( !(languageSourceSet instanceof ISourceSet) )
				continue;

			// --------------------- //

			sourceSets.add( (ISourceSet) languageSourceSet );
		}
	}
}
