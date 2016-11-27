package at.dotpoint.gradle.cross.transform.model;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;

/**
 * Created by RK on 2016-11-26.
 */
public class TaskTransformationData implements ITaskTransformationData
{
	private IApplicationBinarySpec binarySpec;

	public TaskTransformationData( IApplicationBinarySpec binarySpec )
	{
		this.binarySpec = binarySpec;
	}

	public IApplicationBinarySpec getBinarySpec()
	{
		return binarySpec;
	}
}
