package at.dotpoint.gradle.cross.configuration.requirement

import at.dotpoint.gradle.cross.configuration.requirement.command.IConfigurationCommand

/**
*  Created by RK on 16.05.2016.
*/
interface IConfigurationRequirement
{
	/**
	 *
	 * @param key
	 * @param value
	 */
	public void add( String key, Object value );

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void set( String key, Object value );

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void remove( String key, Object value );
}

/**
 *
 */
interface IConfigurationRequirementInternal extends IConfigurationRequirement
{
	/**
	 *
	 * @return
	 */
	ArrayList<IConfigurationCommand> getConfigurationCommands();
}