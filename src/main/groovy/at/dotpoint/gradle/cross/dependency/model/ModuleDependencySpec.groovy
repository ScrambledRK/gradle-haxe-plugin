package at.dotpoint.gradle.cross.dependency.model
/**
 * Created by RK on 2016-08-21.
 */
class ModuleDependencySpec implements IModuleDependencySpecInternal
{
	protected String module;
	protected String group;
	protected String version;

	@Override
	String getModule()
	{
		return module
	}

	@Override
	void setModule( String module )
	{
		this.module = module
	}

	@Override
	String getGroup()
	{
		return group
	}

	@Override
	void setGroup( String group )
	{
		this.group = group
	}

	@Override
	String getVersion()
	{
		return version
	}

	@Override
	void setVersion( String version )
	{
		this.version = version
	}

	/**
	 * The human friendly name of this dependency.
	 *
	 * @return human friendly name
	 */
	@Override
	String getDisplayName()
	{
		return this.group + ":" + this.module + ":" + this.version;
	}
}
