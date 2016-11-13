package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement;

/**
 * Created by RK on 2016-11-13.
 */
public interface ITestComponentSpecInternal extends ITestComponentSpec
{
	//
	void setOptions( IOptionsRequirement configuration );
}
