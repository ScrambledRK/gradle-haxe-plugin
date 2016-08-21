package at.dotpoint.gradle.cross.configuration.builder

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec

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
	IConfiguration build( IApplicationBinarySpec binarySpec );
}