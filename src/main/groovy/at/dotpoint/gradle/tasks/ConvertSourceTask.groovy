package at.dotpoint.gradle.tasks

import at.dotpoint.gradle.platform.HaxePlatform
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by RK on 25.02.16.
 */
class ConvertSourceTask extends SourceTask
{
	private HaxePlatform inputPlatform;
	private HaxePlatform outputPlatform;

	HaxePlatform getInputPlatform() {
		return inputPlatform
	}

	void setInputPlatform(HaxePlatform inputPlatform) {
		this.inputPlatform = inputPlatform
	}

	HaxePlatform getOutputPlatform() {
		return outputPlatform
	}

	void setOutputPlatform(HaxePlatform outputPlatform) {
		this.outputPlatform = outputPlatform
	}

	@TaskAction public void convert()
	{
		println( "in:" + inputPlatform );
		println( "out:" + outputPlatform );
	}

 }