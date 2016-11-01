package at.dotpoint.gradle.cross.variant.model.buildtype;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import org.gradle.model.Managed;

/**
 * Created by RK on 11.03.16.
 */
public interface IBuildType extends IVariant {

}

/**
 *
 */
@Managed
//
public interface IBuildTypeInternal extends IBuildType
{

}
