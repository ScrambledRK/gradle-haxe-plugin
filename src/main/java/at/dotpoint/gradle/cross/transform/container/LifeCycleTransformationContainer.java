package at.dotpoint.gradle.cross.transform.container;

import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformation;
import org.gradle.api.internal.DefaultDomainObjectSet;
/**
 * Created by RK on 03.07.2016.
 */
public class LifeCycleTransformationContainer extends DefaultDomainObjectSet<ILifeCycleTransformation>
{

	public LifeCycleTransformationContainer()
	{
		super( ILifeCycleTransformation.class );
	}

	// (Class<IConvertTransform>) new TypeToken<IConvertTransform>(){}.getRawType()
}
