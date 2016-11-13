package at.dotpoint.gradle.haxe.transform.java;

import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransformData;
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask;
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask;

/**
 * Created by RK on 2016-09-02.
 */
public class JavaTransformData extends ALifeCycleTransformData
{
	public GenerateHXMLTask generateHXMLTask;
	public ExecuteHXMLTask executeHXMLTask;
}
