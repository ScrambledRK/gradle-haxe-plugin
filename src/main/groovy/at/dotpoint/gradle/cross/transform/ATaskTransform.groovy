package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.IVariationsTarget
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 08.07.2016.
 */
abstract class ATaskTransform<TTarget, TInput> implements ITaskTransform<TTarget, TInput>
{

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	VariantCombination<IVariant> canTransform( TTarget target, TInput input )
	{
		if( !this.isValidTransformTarget( target ) || !this.isValidTransformInput( input ) )
			return new VariantCombination<IVariant>();

		return this.getApplicableVariations( input );
	}

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	protected VariantCombination<IVariant> getApplicableVariations( TInput input )
	{
		if( input instanceof Iterable<IVariant> )
		{
			VariantCombination<IVariant> result = new VariantCombination<>();

			for( IVariant variant : input )
			{
				if( this.isValidVariant( variant ) )
					result.add( variant );
			}

			return result;
		}
		else if( input instanceof IVariationsTarget )
		{
			return this.getApplicableVariations( (IVariationsTarget)input.getTargetVariantCombination() );
		}

		return new VariantCombination<IVariant>();
	}

	//
	abstract protected boolean isValidTransformTarget( TTarget target );

	//
	abstract protected boolean isValidTransformInput( TInput target );

	//
	abstract protected boolean isValidVariant( IVariant variant );

	/**
	 *
	 * @param target
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	abstract Task createTransformTask( TTarget target, TInput input, TaskContainer taskContainer );
}
