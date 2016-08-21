package at.dotpoint.gradle.cross.dependency.model

/**
 * Created by RK on 2016-08-21.
 */
interface IModuleDependencySpec extends IDependencySpec
{

	String getModule()

	String getGroup()

	String getVersion()
}

interface IModuleDependencySpecInternal extends IModuleDependencySpec
{

	void setModule( String module )

	void setGroup( String group )

	void setVersion( String version )
}

