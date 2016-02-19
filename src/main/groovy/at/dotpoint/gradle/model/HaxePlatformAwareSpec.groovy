package at.dotpoint.gradle.model

import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationSpec
import org.gradle.platform.base.PlatformAwareComponentSpec
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.component.BaseComponentSpec
import org.gradle.platform.base.internal.DefaultPlatformRequirement
import org.gradle.platform.base.internal.PlatformAwareComponentSpecInternal
import org.gradle.platform.base.internal.PlatformRequirement

/**
 *
 */
@HasInternalProtocol
//
public interface HaxePlatformAwareSpec extends PlatformAwareComponentSpec
{

}

/**
 *
 */
@Managed
//
public interface HaxePlatformAwareSpecInternal extends HaxePlatformAwareSpec, PlatformAwareComponentSpecInternal
{

}

/**
 *
 */
public class DefaultHaxePlatformAwareSpec extends BaseComponentSpec implements HaxePlatformAwareSpecInternal, ApplicationSpec
{

    //
    protected final ArrayList<PlatformRequirement> targetPlatformList = new ArrayList<PlatformRequirement>();

    @Override
    protected String getTypeName()
    {
        return "HaxeApplication";
    }


    @Override
    void targetPlatform( String targetPlatform )
    {
        this.targetPlatformList.add( DefaultPlatformRequirement.create( targetPlatform ) );
    }

    @Override
    List<PlatformRequirement> getTargetPlatforms()
    {
        return Collections.unmodifiableList( this.targetPlatformList );
    }

    @Override
    Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes()
    {
        return Collections.emptySet();
    }
}

