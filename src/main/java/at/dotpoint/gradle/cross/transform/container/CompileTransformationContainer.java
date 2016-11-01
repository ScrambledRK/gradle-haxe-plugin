package at.dotpoint.gradle.cross.transform.container;

import at.dotpoint.gradle.cross.transform.model.compile.ICompileTransform;
import org.gradle.api.internal.DefaultDomainObjectSet;
/**
 * Created by RK on 03.07.2016.
 */
public class CompileTransformationContainer extends DefaultDomainObjectSet<ICompileTransform>
{

	CompileTransformationContainer()
	{
		super( ICompileTransform.class );
	}

	// (Class<IConvertTransform>) new TypeToken<IConvertTransform>(){}.getRawType()
}
