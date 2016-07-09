package at.dotpoint.gradle.cross.transform.compile

import org.gradle.api.internal.DefaultDomainObjectSet
/**
 * Created by RK on 03.07.2016.
 */
class CompileTransformationContainer extends DefaultDomainObjectSet<ICompileTransform>
{

	CompileTransformationContainer()
	{
		super( ICompileTransform.class );
	}

	// (Class<IConvertTransform>) new TypeToken<IConvertTransform>(){}.getRawType()
}
