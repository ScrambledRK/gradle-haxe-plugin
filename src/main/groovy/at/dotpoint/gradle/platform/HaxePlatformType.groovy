package at.dotpoint.gradle.platform

/**
 * Created by RK on 19.02.16.
 */
public enum HaxePlatformType
{
    HAXE("haxe"),
    JAVA("java"),
    ACTIONSCRIPT("actionscript");

    // --------------------------- //
    // --------------------------- //

    private final String ID;

    //
    private HaxePlatformType(final String id)
    {
        this.ID = id;
    }

    /**
     *
     */
    public static HaxePlatformType createFromString( String name )
    {
        if( name == null || name.length() == 0 )
            return null;

        for( HaxePlatformType type : HaxePlatformType.values() )
        {
            if( name.equalsIgnoreCase( type.ID ) )
                return type;
        }
    }

    // --------------------------- //
    // --------------------------- //

    @Override
    public String toString() {
        return this.ID;
    }
}