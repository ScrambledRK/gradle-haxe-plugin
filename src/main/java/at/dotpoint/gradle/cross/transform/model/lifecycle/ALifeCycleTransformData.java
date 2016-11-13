package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpecInternal;

import java.io.File;

/**
 * Created by RK on 2016-08-27.
 */
public class ALifeCycleTransformData implements ILifeCycleTransformData
{
	//
	protected IApplicationBinarySpecInternal binarySpec;

	//
	protected File convertOutputDirectory;
	protected File compileOutputDirectory;

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

	public File getConvertOutputDirectory()
	{
		return convertOutputDirectory;
	}

	public void setConvertOutputDirectory( File convertOutputDirectory )
	{
		this.convertOutputDirectory = convertOutputDirectory;
	}

	public File getCompileOutputDirectory()
	{
		return compileOutputDirectory;
	}

	public void setCompileOutputDirectory( File compileOutputDirectory )
	{
		this.compileOutputDirectory = compileOutputDirectory;
	}
}
