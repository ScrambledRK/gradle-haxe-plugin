package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.variant.iterator.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
*  Created by RK on 02.07.2016.
*/
abstract class AConvertTransform implements IConvertTransform
{

	/**
	 * determines if this CrossConvertTransform can actually transform the given SourceSet to the requested
	 * target variations. returns a list of all the variations it can take into account. a CrossConvertTransform
	 * respecting all variations is taken over a CrossConvertTransform only considering one or two.
	 *
	 * @param sourceSet SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @return Variation this CrossConvertTransform can take into account for the given input
	 */
	@Override
	VariantCombination<IVariant> canTransform( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation )
	{
		VariantCombination<IVariant> result = new VariantCombination<>();

		if( !this.isValidInputSourceSet( sourceSet) )
			return result;

		// -------------------------- //

		IPlatform targetPlatform = targetVariation.getVariant( IPlatform.class );
		IFlavor targetFlavor 	 = targetVariation.getVariant( IFlavor.class );

		if( this.isValidTargetPlatform( targetPlatform ) )
			result.add( targetPlatform );

		if( this.isValidTargetFlavor( targetFlavor ) )
			result.add( targetFlavor );

		return result;
	}

	//
	abstract protected boolean isValidInputSourceSet( ISourceSet sourceSet );

	//
	abstract protected boolean isValidTargetPlatform( IPlatform platform );

	//
	abstract protected boolean isValidTargetFlavor(IFlavor platform );

	/**
	 * actually creates all required tasks to convert a given SourceSet to the requested target variations. these
	 * tasks are not necessarily unique for each BinarySpec but are in combination of SourceSet and target variations.
	 *
	 * @param sourceSet SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @param taskContainer container to create the tasks in, beware of naming
	 * @return top-level task to depend the conversion LifeCycle step on
	 */
	@Override
	abstract Task createTransformTask( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation, TaskContainer taskContainer );

}
