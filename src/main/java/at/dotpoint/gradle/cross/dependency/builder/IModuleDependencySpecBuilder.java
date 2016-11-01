package at.dotpoint.gradle.cross.dependency.builder;

import at.dotpoint.gradle.cross.dependency.model.IModuleDependencySpec;
/**
 * Created by RK on 2016-08-21.
 */
public interface IModuleDependencySpecBuilder extends IDependencySpecBuilder
{

	String getModule();

	void setModule( String module );

	String getGroup();

	void setGroup( String group );

	String getVersion();

	void setVersion( String version );

	@Override
	IModuleDependencySpec build();
}