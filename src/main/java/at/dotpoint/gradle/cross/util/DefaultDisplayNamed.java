package at.dotpoint.gradle.cross.util;

public class DefaultDisplayNamed extends DefaultNamed implements IDisplayNamed
{
	protected String displayName;

	public DefaultDisplayNamed( String name, String displayName ) {
		super(name);
		this.displayName = displayName;
	}

	/**
	 * Returns a human consumable name for this platform.
	 *
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}


	@Override
	public String toString()
	{
		return "[" + this.getClass().getSimpleName() + ";" + this.name + ","+ this.displayName + "]";
	}
}
