package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.variant.iterator.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
*  Created by RK on 02.07.2016.
*/
public interface IConvertTransform
{

	/**
	 * determines if this CrossConvertTransform can actually transform the given SourceSet to the requested
	 * target variations. returns a list of all the variations it can take into account. a CrossConvertTransform
	 * respecting all variations is taken over a CrossConvertTransform only considering one or two.
	 *
	 * @param sourceSet			SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation	IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @return					Variation this CrossConvertTransform can take into account for the given input
	 */
	VariantCombination<IVariant> canTransform( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation );

	/**
	 * actually creates all required tasks to convert a given SourceSet to the requested target variations. these
	 * tasks are not necessarily unique for each BinarySpec but are in combination of SourceSet and target variations.
	 *
	 * @param sourceSet			SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation	IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @param taskContainer		container to create the tasks in, beware of naming
	 * @return					top-level task to depend the conversion LifeCycle step on
	 */
	Task createTransformTask( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation, TaskContainer taskContainer );

}