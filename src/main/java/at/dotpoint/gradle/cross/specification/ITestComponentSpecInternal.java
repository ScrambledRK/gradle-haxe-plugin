package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;

/**
 * Created by RK on 2016-11-13.
 */
public interface ITestComponentSpecInternal extends ITestComponentSpec
{
	//
	void setOptions( IOptions configuration );

	//
	void setMain( String main );
}
