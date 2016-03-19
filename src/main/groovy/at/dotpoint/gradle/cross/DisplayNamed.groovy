package at.dotpoint.gradle.cross

import org.gradle.api.Named


/**
 * Created by RK on 11.03.16.
 */
public interface IDisplayNamed extends Named {

	/**
	 * Returns a human consumable name for this platform.
	 *
	 */
	String getDisplayName();

}

public class DefaultNamed implements Named
{
	protected String name;

	public DefaultNamed( String name ) {
		this.name = name;
	}

	@Override
	String getName() {
		return this.name;
	}
}

public class DefaultDisplayNamed extends DefaultNamed implements IDisplayNamed
{
	protected String displayName;

	public DefaultDisplayNamed( String name, String displayName ) {
		super(name)
		this.displayName = displayName;
	}

	/**
	 * Returns a human consumable name for this platform.
	 *
	 */
	@Override
	String getDisplayName() {
		return this.displayName;
	}

}