package at.dotpoint.gradle.model

import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationSpec

/**
 * software component that should be built
 */
@Managed
public interface HaxeApplicationSpec extends ApplicationSpec, HaxeTargetPlatformSpec
{

}

/**
 *
 */
@Managed
public interface HaxeApplicationSpecInternal extends HaxeApplicationSpec, HaxeTargetPlatformSpecInternal
{

}