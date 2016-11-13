package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.model.ITaskTransform;

import java.util.ArrayList;

/**
 *
 * @param <TTarget>
 * @param <TInput>
 */
public abstract class ATransformationBuilder<TTarget,TInput> implements ITransformBuilder
{
	/**
	 *
	 */
	protected ArrayList<? extends ITaskTransform<TTarget,TInput>> transformList;

	/**
	 *
	 */
	protected ArrayList<AssignedTransform<TTarget,TInput>> assignedTransforms;

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 */
	public ATransformationBuilder( ArrayList<? extends ITaskTransform<TTarget, TInput>> transformList )
	{
		this.assignedTransforms = new ArrayList<>();
		this.transformList = transformList;
	}

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 */
	abstract public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec );

	/**
	 */
	protected AssignedTransform<TTarget,TInput> getAssignedTransform( TTarget target,
	                                                                  TInput input )
	{
		AssignedTransform<TTarget,TInput> assignedTransform = this.getAssignedTransform( target );

		if( assignedTransform != null && assignedTransform.input.equals( input ) )
			return assignedTransform;

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget,TInput> getAssignedTransform( TTarget target )
	{
		for( AssignedTransform<TTarget,TInput> transform : this.assignedTransforms )
		{
			if( transform.target.equals( target )  )
				return transform;
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget,TInput> assignTransformation( TTarget target,
	                                                                  TInput input )
	{
		for( ITaskTransform<TTarget,TInput> transform : this.transformList )
		{
			if( transform.canTransform( target, input ) )
				return this.createAssignedTransform( target, input, transform );
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget,TInput> createAssignedTransform( TTarget target,
	                                                                     TInput input,
	                                                                     ITaskTransform<TTarget,TInput> transform )
	{
		AssignedTransform<TTarget,TInput> assignedTransform = new AssignedTransform<>( target, input, transform );

		// for what ever reason it might go wrong ...
		boolean success = this.assignedTransforms.add( assignedTransform );

		if( success )
			return assignedTransform;

		return null;
	}

	/**
	 */
	protected void performTaskCreation( AssignedTransform<TTarget,TInput> assigned )
	{
		assigned.task = assigned.transform.createTransformTask( assigned.target, assigned.input );
	}

}

