package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.variant.model.platform.Platform
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by RK on 25.02.16.
 */
class ConvertSourceTask extends SourceTask
{
	private Platform inputPlatform;
	private Platform outputPlatform;

	Platform getInputPlatform() {
		return inputPlatform
	}

	void setInputPlatform(Platform inputPlatform) {
		this.inputPlatform = inputPlatform
	}

	Platform getOutputPlatform() {
		return outputPlatform
	}

	void setOutputPlatform(Platform outputPlatform) {
		this.outputPlatform = outputPlatform
	}

	@TaskAction public void convert()
	{
		println( "in:" + inputPlatform );
		println( "out:" + outputPlatform );
	}

 }