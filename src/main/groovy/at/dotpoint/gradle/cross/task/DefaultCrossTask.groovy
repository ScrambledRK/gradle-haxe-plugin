package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.api.DefaultTask

/**
 * Created by RK on 02.07.2016.
 */
class DefaultCrossTask extends DefaultTask implements ICrossTask
{

	private IPlatform inputPlatform;
	private IPlatform outputPlatform;

	/**
	 *
	 * @return
	 */
	@Override
	IPlatform getInputPlatform() {
		return this.inputPlatform
	}

	@Override
	void setInputPlatform(IPlatform inputPlatform) {
		this.inputPlatform = inputPlatform;
	}

	/**
	 *
	 * @return
	 */
	@Override
	IPlatform getOutputPlatform() {
		return this.outputPlatform;
	}

	@Override
	void setOutputPlatform(IPlatform outputPlatform) {
		this.outputPlatform = outputPlatform;
	}
}
