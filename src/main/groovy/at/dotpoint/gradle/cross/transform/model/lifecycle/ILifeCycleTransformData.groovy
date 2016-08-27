package at.dotpoint.gradle.cross.transform.model.lifecycle

import at.dotpoint.gradle.cross.sourceset.ISourceSet

/**
 * Created by RK on 2016-08-27.
 */
interface ILifeCycleTransformData
{
	List<ISourceSet> getSourceSets();
}