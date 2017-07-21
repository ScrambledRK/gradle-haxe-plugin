package at.dotpoint.gradle.cross.dependency.model;

/**
 * Created by RK on 2016-08-21.
 */
// TODO: rename artifact dependency spec?
public interface IModuleDependencySpec extends IDependencySpec
{

	String getModule();

	String getGroup();

	String getVersion();
}

