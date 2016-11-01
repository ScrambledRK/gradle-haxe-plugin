package at.dotpoint.gradle.haxe.specification;

import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec;
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal;
import org.gradle.internal.HasInternalProtocol;

/**
 * Created by RK on 28.03.2016.
 */
@HasInternalProtocol
//
public interface IHaxeComponentSpec extends IApplicationComponentSpec
{

}

public interface IHaxeComponentSpecInternal extends IHaxeComponentSpec, IApplicationComponentSpecInternal
{

}