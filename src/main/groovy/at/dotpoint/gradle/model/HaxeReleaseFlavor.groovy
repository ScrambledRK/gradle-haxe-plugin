package at.dotpoint.gradle.model

import org.gradle.api.Named
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed

/**
 *
 */
@HasInternalProtocol
//
public interface HaxeReleaseFlavor extends Named
{
	String getDisplayName();
}

/**
 *
 */
@Managed
//
public interface HaxeReleaseFlavorInternal extends HaxeReleaseFlavor {
}

/**
 *
 */
public class DefaultHaxeReleaseFlavor implements HaxeReleaseFlavorInternal
{

	//
    private final HaxeReleaseFlavorType releaseFlavorType;

    // ----------------------------------------- //
    // ----------------------------------------- //

    public DefaultHaxeReleaseFlavor( String releaseFlavorType )
    {
        this( HaxeReleaseFlavorType.createFromString( releaseFlavorType ) )
    }

    public DefaultHaxeReleaseFlavor( HaxeReleaseFlavorType releaseFlavorType)
    {
        assert( releaseFlavorType != null );
        this.releaseFlavorType = releaseFlavorType;
    }

    // ----------------------------------------- //
    // ----------------------------------------- //

    @Override
    String getDisplayName()
    {
        return "Haxe ReleaseFlavor " + this.getName();
    }

    @Override
    String getName()
    {
        return this.releaseFlavorType.toString();
    }

    String toString()
    {
        return "[" + this.getName() + "]";
    }
}