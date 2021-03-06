package at.dotpoint.gradle.cross.transform.builder.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.builder.ATransformationBuilder;
import at.dotpoint.gradle.cross.transform.builder.AssignedTransform;
import at.dotpoint.gradle.cross.transform.container.LifeCycleTransformationContainer;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformationDataInternal;
import at.dotpoint.gradle.cross.transform.model.lifecycle.LifeCycleTransformationData;
import com.google.common.collect.Lists;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Created by RK on 03.07.2016.
 */
public class LifeCycleTransformationBuilder extends ATransformationBuilder<ILifeCycleTransformationDataInternal>
{
	//
	private static final Logger LOGGER = Logging.getLogger(LifeCycleTransformationBuilder.class);

	// ------------------------------------------------- //
	// ------------------------------------------------- //

	/**
	 */
	public LifeCycleTransformationBuilder( LifeCycleTransformationContainer lifeCycleTransformationContainer )
	{
		super( Lists.newArrayList( lifeCycleTransformationContainer ) );
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //
	// create:

	/**
	 */
	public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		LOGGER.info( "createTransformationTasks. " + binarySpec );

		// --------------------- //
		// already assigned?

		AssignedTransform<ILifeCycleTransformationDataInternal> assigned = this.getAssignedTransform( binarySpec );

		if( assigned != null )
			throw new RuntimeException("LifeCycleTransformation already set for: " + binarySpec );

		// --------------------- //
		// can transform?

		AssignedTransform<ILifeCycleTransformationDataInternal> result = this.assignTransformation( binarySpec );

		if( result == null )
		{
			LOGGER.error( "cannot assign LifeCycleTransformation: " +  binarySpec );
			return;
		}

		// --------------------- //
		// transform

		this.performTaskCreation( result );
	}

	@Override
	protected ILifeCycleTransformationDataInternal createTaskTransformationData( IApplicationBinarySpec binarySpec )
	{
		return new LifeCycleTransformationData( binarySpec );
	}

	// ------------------------------------------------- //
	// ------------------------------------------------- //
	// update:

	public void updateTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		AssignedTransform<ILifeCycleTransformationDataInternal> assigned = this.getAssignedTransform( binarySpec );

		if( assigned == null )
			throw new RuntimeException("LifeCycleTransformation never been created for: " + binarySpec );

		assigned.transform.updateTransformTask( assigned.target );
	}
}
