package at.dotpoint.gradle.cross.options.requirement;

import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand;

import java.util.ArrayList;

/**
*  Created by RK on 16.05.2016.
*/
public interface IOptionsRequirement
{
	/**
	 *
	 * @param key
	 * @param value
	 */
	void add( String key, Object value );

	/**
	 *
	 * @param key
	 * @param value
	 */
	void set( String key, Object value );

	/**
	 *
	 * @param key
	 * @param value
	 */
	void remove( String key, Object value );
}

