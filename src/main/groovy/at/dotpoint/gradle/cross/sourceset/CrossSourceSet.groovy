package at.dotpoint.gradle.cross.sourceset

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.model.Managed
import org.gradle.platform.base.DependencySpecContainer
import org.gradle.platform.base.internal.DefaultDependencySpecContainer

/**
 * Created by RK on 27.03.2016.
 */
@Managed
//
public class CrossSourceSet extends BaseLanguageSourceSet implements ISourceSetInternal
{

    private IPlatform platform;
    private PlatformRequirement platformRequirement;

    private final DefaultDependencySpecContainer dependencies = new DefaultDependencySpecContainer();

    // --------------------------------------------------- //
    // --------------------------------------------------- //

    CrossSourceSet()
    {
        this( null, null );
    }

    CrossSourceSet( IPlatform platform, PlatformRequirement platformRequirement )
    {
        this.platform = platform
        this.platformRequirement = platformRequirement
    }

    // --------------------------------------------------- //
    // --------------------------------------------------- //

    @Override
    public void platform( Object platformRequirements )
    {
        PlatformRequirement requirement = PlatformNotationParser.getInstance().parseNotation( platformRequirements );

        if( requirement != null )
            this.platformRequirement = requirement;
    }

    PlatformRequirement getPlatformRequirement()
    {
        return platformRequirement
    }

    /**
     *
     */
    void setSourcePlatform( IPlatform platform )
    {
        this.platform = platform;
    }

    IPlatform getSourcePlatform()
    {
        return this.platform;
    }

    @Override
    protected String getLanguageName()
    {

        if( this.platform != null )
            return this.platform.getName() + "Source";

        return super.getLanguageName()
    }

    // --------------------------------------------------- //
    // --------------------------------------------------- //

    @Override
    public DependencySpecContainer getDependencies()
    {
        return dependencies;
    }

    /**
     * IFlavor, IPlatform, IBuildType
     */
    @Override
    void setTargetVariantCombination( VariantCombination<IVariant> combination )
    {
        this.platform = combination.getVariant( IPlatform.class );
    }

    /**
     * IFlavor, IPlatform, IBuildType
     */
    @Override
    VariantCombination<IVariant> getTargetVariantCombination()
    {
        VariantCombination<IVariant> container = new VariantCombination<IVariant>();

        if( this.platform != null )
            container.add( this.platform );

        return container;
    }
}