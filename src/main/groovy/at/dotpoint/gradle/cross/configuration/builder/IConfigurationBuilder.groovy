package at.dotpoint.gradle.cross.configuration.builder

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirementInternal

/**
 * Created by RK on 16.05.2016.
 */
interface IConfigurationBuilder
{

	/**
	 *
	 * @param requirement
	 * @return
	 */
	public IConfiguration build( Iterable<IConfigurationRequirementInternal> requirement );
}