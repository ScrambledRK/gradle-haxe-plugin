package at.dotpoint.gradle.model

import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationSpec
import org.gradle.platform.base.Platform
import org.gradle.platform.base.PlatformAwareComponentSpec
import org.gradle.platform.base.PlatformContainer
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.component.BaseComponentSpec
import org.gradle.platform.base.internal.DefaultPlatformContainer
import org.gradle.platform.base.internal.DefaultPlatformRequirement
import org.gradle.platform.base.internal.PlatformAwareComponentSpecInternal
import org.gradle.platform.base.internal.PlatformRequirement
import org.gradle.play.internal.DefaultPlayPlatformAwareComponentSpec

/**
 *
 */
public interface HaxeTargetPlatformSpec extends PlatformAwareComponentSpec
{

}

/**
 *
 */
@Managed
//
public interface HaxeTargetPlatformSpecInternal extends HaxeTargetPlatformSpec, PlatformAwareComponentSpecInternal
{

}

/**
 *
 */
public class DefaultHaxeTargetPlatformSpec extends BaseComponentSpec implements HaxeTargetPlatformSpecInternal, ApplicationSpec
{

    //
    private final List<PlatformRequirement> targetPlatforms = new ArrayList<PlatformRequirement>();

    @Override
    void targetPlatform( String targetPlatform )
    {
        this.targetPlatforms.add( DefaultPlatformRequirement.create(targetPlatform) );
    }

    @Override
    List<PlatformRequirement> getTargetPlatforms()
    {
        return Collections.unmodifiableList(targetPlatforms);
    }

    @Override
    Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes()
    {
        return Collections.emptySet();
    }
}

