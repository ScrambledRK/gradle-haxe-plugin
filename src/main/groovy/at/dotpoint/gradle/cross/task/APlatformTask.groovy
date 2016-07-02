package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.DefaultTask
/**
 * Created by RK on 25.02.16.
 */
class APlatformTask extends DefaultTask
{
	private IPlatform inputPlatform;
	private IPlatform outputPlatform;

	/**
	 *
	 * @return
	 */
	IPlatform getInputPlatform()
	{
		return inputPlatform
	}

	void setInputPlatform( IPlatform inputPlatform )
	{
		this.inputPlatform = inputPlatform
	}

	/**
	 *
	 * @return
	 */
	IPlatform getOutputPlatform()
	{
		return outputPlatform
	}

	void setOutputPlatform( IPlatform outputPlatform )
	{
		this.outputPlatform = outputPlatform
	}

}