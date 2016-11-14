package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.model.ITaskTransformation;

import java.util.ArrayList;

/**
 */
public abstract class ATransformationBuilder<TTarget> implements ITransformationBuilder
{
	/**
	 *
	 */
	protected ArrayList<? extends ITaskTransformation<TTarget>> transformList;

	/**
	 *
	 */
	protected ArrayList<AssignedTransform<TTarget>> assignedTransforms;

	// ------------------------------------ //
	// ------------------------------------ //

	/**
	 */
	public ATransformationBuilder( ArrayList<? extends ITaskTransformation<TTarget>> transformList )
	{
		this.assignedTransforms = new ArrayList<>();
		this.transformList = transformList;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	abstract public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec );

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	protected AssignedTransform<TTarget> getAssignedTransform( TTarget target )
	{
		for( AssignedTransform<TTarget> transform : this.assignedTransforms )
		{
			if( transform.target.equals( target )  )
				return transform;
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget> assignTransformation( TTarget target )
	{
		for( ITaskTransformation<TTarget> transform : this.transformList )
		{
			if( transform.canTransform( target ) )
				return this.createAssignedTransform( target, transform );
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget> createAssignedTransform( TTarget target,
	                                                              ITaskTransformation<TTarget> transform )
	{
		AssignedTransform<TTarget> assignedTransform = new AssignedTransform<>( target, transform );

		// for what ever reason it might go wrong ...
		boolean success = this.assignedTransforms.add( assignedTransform );

		if( success )
			return assignedTransform;

		return null;
	}

	/**
	 */
	protected void performTaskCreation( AssignedTransform<TTarget> assigned )
	{
		assigned.task = assigned.transform.createTransformTask( assigned.target );
	}

}

