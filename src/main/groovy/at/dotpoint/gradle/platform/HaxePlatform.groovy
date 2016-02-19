package at.dotpoint.gradle.platform

import org.gradle.model.Managed
import org.gradle.platform.base.Platform

/**
 *
 */
@Managed
public interface HaxePlatform extends Platform {
}

/**
 *
 */
public interface HaxePlatformInternal extends HaxePlatform {
}

/**
 *
 */
public class DefaultHaxePlatform implements HaxePlatformInternal
{

    private final HaxePlatformType platformType;

    // ----------------------------------------- //
    // ----------------------------------------- //

    public DefaultHaxePlatform( String platformName )
    {
        this( HaxePlatformType.createFromString( platformName ) )
    }

    public DefaultHaxePlatform( HaxePlatformType platformType )
    {
        assert( platformType != null );

        this.platformType = platformType;
    }

    // ----------------------------------------- //
    // ----------------------------------------- //

    @Override
    String getDisplayName()
    {
        return "Haxe Platform " + this.platformType.toString();
    }

    @Override
    String getName()
    {
        return this.platformType.toString();
    }
}