package at.dotpoint.gradle.cross.transform.builder;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.model.ITaskTransformation;
import at.dotpoint.gradle.cross.transform.model.ITaskTransformationData;

import java.util.ArrayList;

/**
 */
public abstract class ATransformationBuilder<TTarget extends ITaskTransformationData> implements ITransformationBuilder
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

	abstract protected TTarget createTaskTransformationData( IApplicationBinarySpec binarySpec );

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	protected AssignedTransform<TTarget> getAssignedTransform( IApplicationBinarySpec binarySpec )
	{
		for( AssignedTransform<TTarget> transform : this.assignedTransforms )
		{
			if( transform.target.getBinarySpec().equals( binarySpec )  )
				return transform;
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget> assignTransformation( IApplicationBinarySpec binarySpec )
	{
		for( ITaskTransformation<TTarget> transform : this.transformList )
		{
			if( transform.canTransform( binarySpec ) )
				return this.createAssignedTransform( binarySpec, transform );
		}

		return null;
	}

	/**
	 */
	protected AssignedTransform<TTarget> createAssignedTransform( IApplicationBinarySpec binarySpec,
	                                                              ITaskTransformation<TTarget> transform )
	{
		TTarget target = this.createTaskTransformationData( binarySpec );
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
		assigned.transform.createTransformTask( assigned.target );
	}

}

