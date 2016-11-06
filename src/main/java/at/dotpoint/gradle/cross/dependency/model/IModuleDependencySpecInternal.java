package at.dotpoint.gradle.cross.dependency.model;

public interface IModuleDependencySpecInternal extends IModuleDependencySpec
{

	void setModule( String module );

	void setGroup( String group );

	void setVersion( String version );
}
