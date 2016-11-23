package at.dotpoint.gradle.cross.util;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.platform.base.SourceComponentSpec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RK on 2016-08-27.
 */
public class BinarySpecUtil
{
	/**
	 */
	public static ArrayList<ISourceSet> getSourceSetList( List<IApplicationBinarySpec> binarySpecList )
	{
		ArrayList<ISourceSet> sourceSets = new ArrayList<>();

		for( IApplicationBinarySpec binarySpec : binarySpecList )
			sourceSets.addAll( BinarySpecUtil.getSourceSetList( binarySpec ) );

		return sourceSets;
	}

	/**
	 */
	public static ArrayList<ISourceSet> getSourceSetList( IApplicationBinarySpec binarySpec )
	{
		ArrayList<ISourceSet> sourceSets = new ArrayList<>();

		populateInputSourceSets( binarySpec.getSources().iterator(), sourceSets );
		populateInputSourceSets( binarySpec.getApplication().getSources().iterator(), sourceSets );

		return sourceSets;
	}

	/**
	 */
	public static ArrayList<ISourceSet> getSourceSetList( SourceComponentSpec testSpec )
	{
		ArrayList<ISourceSet> sourceSets = new ArrayList<>();

		populateInputSourceSets( testSpec.getSources().iterator(), sourceSets );

		return sourceSets;
	}

	/**
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
