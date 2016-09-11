package at.dotpoint.gradle.cross.options.requirement

import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand

/**
*  Created by RK on 16.05.2016.
*/
interface IOptionsRequirement
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
interface IOptionsRequirementInternal extends IOptionsRequirement
{
	/**
	 *
	 * @return
	 */
	ArrayList<IOptionsCommand> getConfigurationCommands();
}