package at.dotpoint.gradle.model;

/**
 * Created by RK on 20.02.16.
 */
public enum HaxeReleaseFlavorType
{
	DEBUG("debug"),
	PRODUCTION("production"),
	RELEASE("release");

    // --------------------------- //
    // --------------------------- //

    private final String ID;

    //
    private HaxeReleaseFlavorType( final String id )
    {
        this.ID = id;
    }

    /**
     *
     */
    public static HaxeReleaseFlavorType createFromString( String name )
    {
        if( name == null || name.length() == 0 )
            return null;

        for( HaxeReleaseFlavorType type : HaxeReleaseFlavorType.values() )
        {
            if( name.equalsIgnoreCase( type.ID ) )
                return type;
        }

		return null;
    }

    // --------------------------- //
    // --------------------------- //

    @Override
    public String toString() {
        return this.ID;
    }
}