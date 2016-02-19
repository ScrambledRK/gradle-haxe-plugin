package at.dotpoint.gradle.platform

import org.gradle.platform.base.internal.PlatformRequirement
import org.gradle.platform.base.internal.PlatformResolver

/**
 * Created by RK on 19.02.16.
 */
public class HaxePlatformResolver implements PlatformResolver<HaxePlatform>
{

    @Override
    Class<HaxePlatform> getType()
    {
        return HaxePlatform.class;
    }

    @Override
    HaxePlatform resolve( PlatformRequirement platformRequirement )
    {
        return new DefaultHaxePlatform( platformRequirement.getPlatformName() )
    }

    HaxePlatform resolve( HaxePlatformRequirement platformRequirement )
    {
        return new DefaultHaxePlatform( platformRequirement );
    }
}
