package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;

import java.util.List;

/**
 * Created by RK on 2016-08-27.
 */
public interface ILifeCycleTransformData
{
	List<ISourceSet> getSourceSets();
}