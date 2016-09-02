package at.dotpoint.gradle.cross.transform.builder.compile

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.transform.builder.ATransformationBuilder
import at.dotpoint.gradle.cross.transform.builder.AssignedTransform
import at.dotpoint.gradle.cross.transform.container.CompileTransformationContainer
import at.dotpoint.gradle.cross.util.BinarySpecUtil
import com.google.common.collect.Lists
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.language.base.LanguageSourceSet
/**
 * Created by RK on 03.07.2016.
 */
class CompileTransformationBuilder extends ATransformationBuilder<IApplicationBinarySpec,List<ISourceSet>>
{
	//
	private static final Logger LOGGER = Logging.getLogger(CompileTransformationBuilder.class)

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param compileTransformationContainer
	 * @param taskContainer
	 */
	CompileTransformationBuilder( CompileTransformationContainer compileTransformationContainer )
	{
		super( Lists.newArrayList( compileTransformationContainer ) );
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 */
	public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		ArrayList<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );

		// ------------- //

		this.createCompileTransformations( binarySpec, sourceSets );
	}

	/**
	 *
	 * @param sourceSets
	 * @param input
	 */
	private void populateInputSourceSets( Iterator<LanguageSourceSet> input,
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

	/**
	 *
	 * @param iterator
	 * @param binarySpec
	 */
	private void createCompileTransformations( IApplicationBinarySpecInternal binarySpec,
	                                           ArrayList<ISourceSet> sourceSets )
	{
		// --------------------- //
		// already assigned?

		AssignedTransform assigned = this.getAssignedTransform( binarySpec, sourceSets );

		if( assigned != null )
		{
			this.performLifeCycle( assigned, binarySpec, CrossPlugin.NAME_COMPILE_SOURCE );
			return;
		}

		// --------------------- //
		// can transform?

		AssignedTransform result = this.assignTransformation( binarySpec, sourceSets );

		if( result == null )
		{
			//LOGGER.error( "cannot assign CompileTransformation", binarySpec, sourceSets );
			return;
		}

		// --------------------- //
		// transform

		this.performTaskCreation( result );
		this.performLifeCycle( result, binarySpec, CrossPlugin.NAME_COMPILE_SOURCE );
	}


}
