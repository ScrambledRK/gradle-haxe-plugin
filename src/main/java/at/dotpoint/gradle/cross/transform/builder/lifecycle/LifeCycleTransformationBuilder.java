package at.dotpoint.gradle.cross.transform.builder.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;
import at.dotpoint.gradle.cross.transform.builder.ATransformationBuilder;
import at.dotpoint.gradle.cross.transform.builder.AssignedTransform;
import at.dotpoint.gradle.cross.transform.container.LifeCycleTransformationContainer;
import at.dotpoint.gradle.cross.transform.model.ITaskTransform;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransform;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformData;
import com.google.common.collect.Lists;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Created by RK on 03.07.2016.
 */
public class LifeCycleTransformationBuilder
		extends ATransformationBuilder<IApplicationBinarySpec,ILifeCycleTransformData>
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

	/**
	 */
	public void createTransformationTasks( IApplicationBinarySpecInternal binarySpec )
	{
		//println "createTransformationTasks. " + binarySpec;

		// --------------------- //
		// already assigned?

		AssignedTransform assigned = this.getAssignedTransform( binarySpec );

		if( assigned != null )  // this might actually happen due to subproject dependencies!
			throw new RuntimeException("LifeCycleTransformation already set for: " + binarySpec );

		// --------------------- //
		// can transform?

		AssignedTransform result = this.assignTransformation( binarySpec );

		if( result == null )
		{
			LOGGER.error( "cannot assign LifeCycleTransformation: " +  binarySpec );
			return;
		}

		// --------------------- //
		// transform

		this.performTaskCreation( result );
	}

	/**
	 */
	protected AssignedTransform<IApplicationBinarySpec,ILifeCycleTransformData> assignTransformation(
			IApplicationBinarySpec target )
	{
		for( ITaskTransform<IApplicationBinarySpec,ILifeCycleTransformData> transform : this.transformList )
		{
			if( !(transform instanceof ILifeCycleTransform) )
				continue;

			if( transform.canTransform( target, null ) )
				return this.createAssignedTransform( target, ( (ILifeCycleTransform) transform ).createTransformData(), transform );
		}

		return null;
	}

}
