package at.dotpoint.gradle.cross.specification.executable

import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed


/**
 * Created by RK on 20.03.16.
 */
@HasInternalProtocol
//
public interface IExecutableComponentSpec extends IApplicationComponentSpec {

}

/**
 *
 */
//
public interface IExecutableComponentSpecInternal extends IApplicationComponentSpecInternal, IExecutableComponentSpec {

}