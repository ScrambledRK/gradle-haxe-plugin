package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform

/**
 * Created by RK on 02.07.2016.
 */
interface IPlatformTask
{
	/**
	 *
	 * @return
	 */
	IPlatform getInputPlatform();
	void setInputPlatform( IPlatform inputPlatform );

	/**
	 *
	 * @return
	 */
	IPlatform getOutputPlatform();
	void setOutputPlatform( IPlatform outputPlatform );
}