package at.dotpoint.gradle.cross.transform.model.lifecycle

import at.dotpoint.gradle.cross.sourceset.ISourceSet

/**
 * Created by RK on 2016-08-27.
 */
class ALifeCycleTransformData implements ILifeCycleTransformData
{
	//
	protected List<ISourceSet> sourceSets;

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	List<ISourceSet> getSourceSets()
	{
		return sourceSets
	}

	/**
	 *
	 * @param sourceSets
	 */
	void setSourceSets( List<ISourceSet> sourceSets )
	{
		this.sourceSets = sourceSets
	}
}
