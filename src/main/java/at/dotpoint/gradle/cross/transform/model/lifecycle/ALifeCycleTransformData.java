package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;

import java.util.List;

/**
 * Created by RK on 2016-08-27.
 */
public class ALifeCycleTransformData implements ILifeCycleTransformData
{
	//
	protected IApplicationBinarySpecInternal binarySpec;

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	public IApplicationBinarySpecInternal getBinarySpec()
	{
		return binarySpec;
	}

	public void setBinarySpec( IApplicationBinarySpecInternal binarySpec )
	{
		this.binarySpec = binarySpec;
	}
}
