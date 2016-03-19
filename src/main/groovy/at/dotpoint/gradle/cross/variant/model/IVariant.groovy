package at.dotpoint.gradle.cross.variant.model

import org.gradle.api.Named

/**
 * Created by RK on 11.03.16.
 */
public interface IVariant extends Named {

	/**
	 * Returns a human consumable name for this platform.
	 *
	 */
	String getDisplayName();

}