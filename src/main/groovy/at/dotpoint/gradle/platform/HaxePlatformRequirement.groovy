package at.dotpoint.gradle.platform

import org.gradle.platform.base.internal.PlatformRequirement

/**
 * Created by RK on 19.02.16.
 */
class HaxePlatformRequirement implements PlatformRequirement
{

    //
    private final HaxePlatformType platformType;

    //
    public HaxePlatformRequirement( HaxePlatformType targetPlatform )
    {
       this.platformType = targetPlatform;
    }

    @Override
    String getPlatformName()
    {
        return this.platformType.toString();
    }

}
