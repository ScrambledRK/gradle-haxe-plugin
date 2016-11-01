package at.dotpoint.gradle.cross.options.builder;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirementInternal;
/**
 * Created by RK on 16.05.2016.
 */
public interface IOptionsBuilder
{

	/**
	 *
	 * @param requirement
	 * @return
	 */
	IOptions build( Iterable<IOptionsRequirementInternal> requirements );
}