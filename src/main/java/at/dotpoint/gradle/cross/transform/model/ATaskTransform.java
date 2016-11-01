package at.dotpoint.gradle.cross.transform.model;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.IVariationsTarget;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.Task;
/**
 * Created by RK on 08.07.2016.
 */
public abstract class ATaskTransform<TTarget, TInput>
		implements ITaskTransform<TTarget, TInput>
{

	/**
	 *
	 * @param target
	 * @param input
	 * @return
	 */
	public boolean canTransform( TTarget target, TInput input )
	{
		return this.isValidTransformTarget( target ) && this.isValidTransformInput( input );

	}

	/**
	 *
	 * @param target
	 * @return
	 */
	abstract protected boolean isValidTransformTarget( TTarget target );

	/**
	 *
	 * @param target
	 * @return
	 */
	abstract protected boolean isValidTransformInput( TInput target );

	/**
	 *
	 * @param target
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	public abstract Task createTransformTask( TTarget target, TInput input );

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

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

	/**
	 *
	 * @param variant
	 * @return
	 */
	protected boolean isValidVariant( IVariant variant )
	{
		return false;
	}

}
