package at.dotpoint.gradle.cross.util;

import org.gradle.api.Named;

public class DefaultNamed implements Named
{
	protected String name;

	public DefaultNamed( String name ) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public String toString()
	{
		return "[" + this.getClass().getSimpleName() + ";" + this.name + "]";
	}
}

