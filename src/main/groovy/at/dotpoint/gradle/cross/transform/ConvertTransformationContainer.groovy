package at.dotpoint.gradle.cross.transform

import org.gradle.api.internal.DefaultDomainObjectSet
/**
 * Created by RK on 03.07.2016.
 */
class ConvertTransformationContainer extends DefaultDomainObjectSet<IConvertTransform>
{

	ConvertTransformationContainer()
	{
		super( IConvertTransform.class );
	}

	// (Class<IConvertTransform>) new TypeToken<IConvertTransform>(){}.getRawType()
}
