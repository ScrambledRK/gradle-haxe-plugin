package at.dotpoint.gradle.cross.dependency.builder;

import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpecInternal;
import at.dotpoint.gradle.cross.dependency.model.ModuleDependencySpec;
/**
 * Created by RK on 2016-08-21.
 */
public class ModuleDependencySpecBuilder implements IModuleDependencySpecBuilder
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

	@Override
	public IModuleDependencySpec build()
	{
		IModuleDependencySpecInternal specInternal = new ModuleDependencySpec();

		specInternal.setGroup( this.group );
		specInternal.setModule( this.module );
		specInternal.setVersion( this.version );

		return specInternal;
	}
}
