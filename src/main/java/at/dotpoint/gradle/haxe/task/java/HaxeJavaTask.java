package at.dotpoint.gradle.haxe.task.java;

import at.dotpoint.gradle.haxe.task.AHaxeTask;

import java.io.File;

/**
 * Created by RK on 2016-11-28.
 */
public class HaxeJavaTask extends AHaxeTask
{
	/**
	 */
	protected String getNativeDependencyOption( File artifact )
	{
		return "-java-lib \"" + artifact.getAbsolutePath() + "\"";
	}

	/**
	 */
	protected String getOutputCommandOption()
	{
		return "-java \"" + this.getOutputDir().getAbsolutePath() + "\"";
	}
}
