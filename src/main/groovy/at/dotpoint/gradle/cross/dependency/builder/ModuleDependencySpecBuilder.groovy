package at.dotpoint.gradle.cross.dependency.builder

import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec
import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpecInternal
import at.dotpoint.gradle.cross.dependency.model.ModuleDependencySpec
/**
 * Created by RK on 2016-08-21.
 */
class ModuleDependencySpecBuilder implements IModuleDependencySpecBuilder
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

	@Override
	IModuleDependencySpec build()
	{
		IModuleDependencySpecInternal specInternal = new ModuleDependencySpec();

		specInternal.group = this.group;
		specInternal.module = this.module;
		specInternal.version = this.version;

		return specInternal;
	}
}
