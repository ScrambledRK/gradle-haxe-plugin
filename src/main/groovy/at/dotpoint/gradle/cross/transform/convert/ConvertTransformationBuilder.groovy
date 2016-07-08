package at.dotpoint.gradle.cross.transform.convert

import at.dotpoint.gradle.cross.CrossPlugin
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import com.google.common.collect.Lists
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskContainer
import org.gradle.language.base.LanguageSourceSet
import org.gradle.api.Task

/**
 * Created by RK on 03.07.2016.
 */
class ConvertTransformationBuilder
{
	//
	private static final Logger LOGGER = Logging.getLogger(ConvertTransformationBuilder.class)

	//
	private ArrayList<IConvertTransform> convertTransforms;

	//
	private ArrayList<AssignedTransform> assignedTransforms;

	//
	private TaskContainer taskContainer;

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param convertTransformationContainer
	 * @param taskContainer
	 */
	ConvertTransformationBuilder( ConvertTransformationContainer convertTransformationContainer, TaskContainer taskContainer)
	{
		this.assignedTransforms = new ArrayList<>();

		this.convertTransforms = Lists.newArrayList( convertTransformationContainer );
		this.taskContainer = taskContainer
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 */
	public void createConvertTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		this.createSourceSetTransformations( binarySpec.sources.iterator(), binarySpec );
		this.createSourceSetTransformations( binarySpec.application.sources.iterator(), binarySpec );
	}

	/**
	 *
	 * @param iterator
	 * @param binarySpec
	 */
	private void createSourceSetTransformations(Iterator<LanguageSourceSet> iterator, IApplicationBinarySpecInternal binarySpec )
	{
		while( iterator.hasNext() )
		{
			LanguageSourceSet languageSourceSet = iterator.next();

			if( !(languageSourceSet instanceof ISourceSet) )
			{
				LOGGER.warn( "cannot create ConvertTransformationTask for LanguageSourceSet", languageSourceSet );
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
				this.performLifeCycle( assigned, binarySpec );
				continue;
			}

			// --------------------- //
			// can transform?

			AssignedTransform result = this.assignTransformation( sourceSet, targetVariation );

			if( result == null )
			{
				LOGGER.error( "cannot assign ConvertTransformation", sourceSet, targetVariation );
				continue;
			}

			// --------------------- //
			// transform

			this.performTaskCreation( result );
			this.performLifeCycle( result, binarySpec );

			this.assignedTransforms.add( result );
		}
	}

	/**
	 *
	 * @param assigned
	 * @param binarySpec
	 */
	private void performTaskCreation( AssignedTransform assigned )
	{
		assigned.task = assigned.transform.createTransformTask( assigned.sourceSet, assigned.targetVariation, this.taskContainer );
	}

	/**
	 *
	 * @param assignedTransform
	 * @param binarySpec
	 */
	private void performLifeCycle( AssignedTransform assignedTransform, IApplicationBinarySpecInternal binarySpec )
	{
		Task convertTask = TaskUtil.findTaskByName( binarySpec.tasks, binarySpec.tasks.taskName( CrossPlugin.NAME_CONVERT_SOURCE) );
			 convertTask.dependsOn assignedTransform.task;
	}

	/**
	 *
	 * @param sourceSet
	 * @param targetVariation
	 * @return
	 */
	private AssignedTransform assignTransformation( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation )
	{
		IConvertTransform bestTransform = null;
		VariantCombination<IVariant> bestVariation = null;

		for( IConvertTransform transform : this.convertTransforms )
		{
			VariantCombination<IVariant> validVariation = transform.canTransform( sourceSet, targetVariation );

			if( this.isBetterTransform( validVariation, bestVariation ) )
			{
				bestTransform = transform;
				bestVariation = validVariation;
			}
		}

		// -------------- //

		if( bestTransform != null )
			return new AssignedTransform( sourceSet, bestVariation, bestTransform );

		return null;
	}

	/**
	 *
	 * @param currentVariation
	 * @param bestVariation
	 * @return
	 */
	private boolean isBetterTransform(VariantCombination<IVariant> currentVariation, VariantCombination<IVariant> bestVariation )
	{
		if ( bestVariation == null || bestVariation.size() < currentVariation.size() )
		{
			if( bestVariation != null && bestVariation.size() == currentVariation.size() )
				LOGGER.warn( "multiple ConvertTransforms found satisfying the target Variations", currentVariation );

			return true;
		}

		return false;
	}

	/**
	 *
	 * @param sourceSet
	 * @param targetVariation
	 * @return
	 */
	private AssignedTransform getAssignedTransform(ISourceSet sourceSet, VariantCombination<IVariant> targetVariation )
	{
		for( AssignedTransform transform : this.assignedTransforms )
		{
			if( transform.sourceSet == sourceSet )
			{
				if( targetVariation.contains( transform.targetVariation ) ) // assigned variation is part of binary var
					return transform;
			}
		}

		return null;
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 *
	 */
	class AssignedTransform
	{
		public ISourceSet sourceSet;
		public VariantCombination<IVariant> targetVariation;
		public IConvertTransform transform;
		public Task task;

		AssignedTransform( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation, IConvertTransform transform )
		{
			this.sourceSet = sourceSet
			this.targetVariation = targetVariation
			this.transform = transform;
		}
	}
}
