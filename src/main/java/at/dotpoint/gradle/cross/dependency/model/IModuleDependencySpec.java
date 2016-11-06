package at.dotpoint.gradle.cross.dependency.model;

/**
 * Created by RK on 2016-08-21.
 */
public interface IModuleDependencySpec extends IDependencySpec
{

	String getModule();

	String getGroup();

	String getVersion();
}

