package at.dotpoint.gradle.cross.transform.builder.convert;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.builder.ATransformationBuilder;
import at.dotpoint.gradle.cross.transform.builder.AssignedTransform;
import at.dotpoint.gradle.cross.transform.container.ConvertTransformationContainer;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import com.google.common.collect.Lists;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.language.base.LanguageSourceSet;

import java.util.Iterator;

/**
 * Created by RK on 03.07.2016.
 */
public class ConvertTransformationBuilder extends ATransformationBuilder<ISourceSet,VariantCombination<IVariant>>
{
	//
	private static final Logger LOGGER = Logging.getLogger(ConvertTransformationBuilder.class);

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param convertTransformationContainer
	 * @param taskContainer
	 */
	ConvertTransformationBuilder( ConvertTransformationContainer convertTransformationContainer )
	{
		super( Lists.newArrayList( convertTransformationContainer ) );
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 */
	public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		this.createSourceSetTransformations( binarySpec.sources.iterator(), binarySpec );
		this.createSourceSetTransformations( binarySpec.application.sources.iterator(), binarySpec );
	}

	/**
	 *
	 * @param iterator
	 * @param binarySpec
	 */
	private void createSourceSetTransformations( Iterator<LanguageSourceSet> iterator,
	                                             IApplicationBinarySpecInternal binarySpec )
	{
		while( iterator.hasNext() )
		{
			LanguageSourceSet languageSourceSet = iterator.next();

			if( !(languageSourceSet instanceof ISourceSet) )
			{
				//LOGGER.warn( "cannot create ConvertTransformationTask for LanguageSourceSet", languageSourceSet );
				continue;
			}

			// --------------------- //

			ISourceSet sourceSet = (ISourceSet) languageSourceSet;
			VariantCombination<IVariant> targetVariation = binarySpec.getTargetVariantCombination();

			// --------------------- //
			// already assigned?

			AssignedTransform assigned = this.getAssignedTransform( sourceSet, targetVariation );

			if( assigned != null )
			{
				this.performLifeCycle( assigned, binarySpec, CrossPlugin.NAME_CONVERT_SOURCE );
				continue;
			}

			// --------------------- //
			// can transform?

			AssignedTransform result = this.assignTransformation( sourceSet, targetVariation );

			if( result == null )
			{
				//LOGGER.error( "cannot assign ConvertTransformation", sourceSet, targetVariation );
				continue;
			}

			// --------------------- //
			// transform

			this.performTaskCreation( result );
			this.performLifeCycle( result, binarySpec, CrossPlugin.NAME_CONVERT_SOURCE );
		}
	}

}