package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.variant.model.platform.Platform
import org.gradle.api.tasks.SourceTask

/**
 * Created by RK on 25.02.16.
 */
class AConvertSourceTask extends SourceTask
{
	private Platform inputPlatform;
	private Platform outputPlatform;

	/**
	 *
	 * @return
	 */
	Platform getInputPlatform()
	{
		return inputPlatform
	}

	void setInputPlatform( Platform inputPlatform )
	{
		this.inputPlatform = inputPlatform
	}

	/**
	 *
	 * @return
	 */
	Platform getOutputPlatform()
	{
		return outputPlatform
	}

	void setOutputPlatform( Platform outputPlatform )
	{
		this.outputPlatform = outputPlatform
	}

}