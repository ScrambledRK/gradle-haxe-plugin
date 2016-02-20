package at.dotpoint.gradle.platform

import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.Platform

/**
 *
 */
@HasInternalProtocol
//
public interface HaxePlatform extends Platform {
}

/**
 *
 */
@Managed
//
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
        return "Haxe Platform " + this.getName();
    }

    @Override
    String getName()
    {
        return this.platformType.toString();
    }

    String toString()
    {
        return "[" + this.getName() + "]";
    }
}