package at.dotpoint.gradle.cross.transform.container

import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransform
import org.gradle.api.internal.DefaultDomainObjectSet
/**
 * Created by RK on 03.07.2016.
 */
class LifeCycleTransformationContainer extends DefaultDomainObjectSet<ILifeCycleTransform>
{

	LifeCycleTransformationContainer()
	{
		super( ILifeCycleTransform.class );
	}

	// (Class<IConvertTransform>) new TypeToken<IConvertTransform>(){}.getRawType()
}
