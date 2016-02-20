package at.dotpoint.gradle.model

import at.dotpoint.gradle.platform.HaxePlatform
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationBinarySpec
import org.gradle.platform.base.Variant
import org.gradle.platform.base.binary.BaseBinarySpec
import org.gradle.platform.base.internal.BinarySpecInternal

/**
 * Created by RK on 19.02.16.
 */
@HasInternalProtocol
//
public interface HaxeApplicationBinarySpec extends ApplicationBinarySpec
{

    /**
     * {@inheritDoc}
     */
    @Override
    HaxeApplicationSpec getApplication();

    /**
    * The Haxe-Target-Platform this binary is built for. (java, python, neko, etc)
    */
    @Variant
    HaxePlatform getTargetPlatform();

	@Variant
	HaxeReleaseFlavor getReleaseType();
}

/**
 *
 */
@Managed
//
public interface HaxeApplicationBinarySpecInternal extends HaxeApplicationBinarySpec, BinarySpecInternal
{
    void setTargetPlatform( HaxePlatform platform );
    void setReleaseType( HaxeReleaseFlavor releaseType );
}

/**
 *
 */
public class DefaultHaxeApplicationBinarySpec extends BaseBinarySpec implements HaxeApplicationBinarySpecInternal, ApplicationBinarySpec
{

    private HaxePlatform platform;
    private HaxeReleaseFlavor releaseType;

    // --------------------------------------------------- //
    // --------------------------------------------------- //

    /**
     *
     */
    void setTargetPlatform( HaxePlatform platform )
    {
        this.platform = platform;
    }

    HaxePlatform getTargetPlatform()
    {
        return this.platform;
    }

	/**
	 *
	 */
	void setReleaseType( HaxeReleaseFlavor releaseType )
	{
		this.releaseType = releaseType;
	}

	HaxeReleaseFlavor getReleaseType()
	{
		return this.releaseType;
	}

    /**
     *
     */
    @Override
    HaxeApplicationSpec getApplication()
    {
        return getComponentAs(HaxeApplicationSpec.class);
    }

}
