package at.dotpoint.gradle.cross.dependency.model;
/**
 * Created by RK on 2016-08-21.
 */
public class ModuleDependencySpec implements IModuleDependencySpecInternal
{
	protected String module;
	protected String group;
	protected String version;

	@Override
	public String getModule()
	{
		return module;
	}

	@Override
	public void setModule( String module )
	{
		this.module = module;
	}

	@Override
	public String getGroup()
	{
		return group;
	}

	@Override
	public void setGroup( String group )
	{
		this.group = group;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public void setVersion( String version )
	{
		this.version = version;
	}

	/**
	 * The human friendly name of this dependency.
	 *
	 * @return human friendly name
	 */
	@Override
	public String getDisplayName()
	{
		return this.group + ":" + this.module + ":" + this.version;
	}

	public String toString()
	{
		return this.getDisplayName();
	}
}
