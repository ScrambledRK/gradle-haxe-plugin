package at.dotpoint.gradle.model

import at.dotpoint.gradle.platform.HaxePlatformNotationParser
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationSpec
import org.gradle.platform.base.internal.PlatformRequirement

/**
 * software component that should be built
 */
@HasInternalProtocol
//
public interface HaxeApplicationSpec extends ApplicationSpec, HaxePlatformAwareSpec
{
    void platform( Object platformRequirements );
}

/**
 *
 */
@Managed
//
public interface HaxeApplicationSpecInternal extends HaxeApplicationSpec, HaxePlatformAwareSpecInternal
{

}

/**
 *
 */
class DefaultHaxeApplicationSpec extends DefaultHaxePlatformAwareSpec implements HaxeApplicationSpecInternal
{

    @Override
    public void platform( Object platformRequirements )
    {
        PlatformRequirement requirement = HaxePlatformNotationParser.getInstance().parseNotation( platformRequirements );

        if( requirement != null )
            this.targetPlatformList.add( requirement );
    }
}