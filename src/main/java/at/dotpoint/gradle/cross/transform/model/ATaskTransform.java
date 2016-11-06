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
	 */
	public boolean canTransform( TTarget target, TInput input )
	{
		return this.isValidTransformTarget( target ) && this.isValidTransformInput( input );

	}

	/**
	 */
	abstract protected boolean isValidTransformTarget( TTarget target );

	/**
	 */
	abstract protected boolean isValidTransformInput( TInput target );

	/**
	 */
	public abstract Task createTransformTask( TTarget target, TInput input );

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	protected VariantCombination<IVariant> getApplicableVariations( Object input )
	{
		if( input instanceof Iterable)
		{
			VariantCombination<IVariant> result = new VariantCombination<>();
			Iterable<?> iterable = (Iterable<?>) input;

			iterable.forEach( variant ->
			{
				if( !(variant instanceof IVariant) )
					return;

				if( ATaskTransform.this.isValidVariant( (IVariant)variant ) )
					result.add( (IVariant)variant );
			} );

			return result;
		}
		else if( input instanceof IVariationsTarget )
		{
			return this.getApplicableVariations( ((IVariationsTarget)input).getTargetVariantCombination() );
		}

		return new VariantCombination<>();
	}

	/**
	 */
	protected boolean isValidVariant( IVariant variant )
	{
		return false;
	}

}
