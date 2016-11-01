package at.dotpoint.gradle.cross.transform.model.lifecycle;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;

import java.util.List;

/**
 * Created by RK on 2016-08-27.
 */
public class ALifeCycleTransformData implements ILifeCycleTransformData
{
	//
	protected List<ISourceSet> sourceSets;

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	public List<ISourceSet> getSourceSets()
	{
		return sourceSets;
	}

	/**
	 *
	 * @param sourceSets
	 */
	public void setSourceSets( List<ISourceSet> sourceSets )
	{
		this.sourceSets = sourceSets;
	}
}
