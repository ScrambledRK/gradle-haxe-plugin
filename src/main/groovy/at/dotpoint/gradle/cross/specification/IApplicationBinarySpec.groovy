package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationBinarySpec
import org.gradle.platform.base.ComponentSpec
import org.gradle.platform.base.Variant
import org.gradle.platform.base.VariantComponentSpec
import org.gradle.platform.base.internal.BinarySpecInternal
import org.gradle.platform.base.internal.HasIntermediateOutputsComponentSpec

/**
 * Created by RK on 11.03.16.
 */
@HasInternalProtocol
//
public interface IApplicationBinarySpec extends ApplicationBinarySpec, HasIntermediateOutputsComponentSpec
{
	//
	@Override
	IApplicationComponentSpec getApplication();

	//
	@Variant
	IPlatform getTargetPlatform();

	//
	@Variant
	IFlavor getTargetFlavor();
}

/**
 * Created by RK on 11.03.16.
 */
//
public interface IApplicationBinarySpecInternal extends IApplicationBinarySpec, BinarySpecInternal
{
	//
	void setTargetPlatform( IPlatform platform );

	//
	void setTargetFlavor( IFlavor releaseType );
}

