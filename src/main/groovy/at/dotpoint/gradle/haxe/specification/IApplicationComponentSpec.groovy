package at.dotpoint.gradle.haxe.specification

/**
 * Created by RK on 28.03.2016.
 */
public interface IApplicationComponentSpec extends at.dotpoint.gradle.cross.specification.IApplicationComponentSpec
{
	//
	void hxml( Object hxmlConfiguration );
}

public interface IApplicationComponentSpecInternal extends IApplicationComponentSpec, at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
{

}