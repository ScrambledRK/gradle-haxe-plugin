package at.dotpoint.gradle.cross.util;

import org.gradle.api.Named;

/**
 * Created by RK on 11.03.16.
 */
public interface IDisplayNamed extends Named
{

	/**
	 * Returns a human consumable name for this sourcePlatform.
	 *
	 */
	String getDisplayName();

}
