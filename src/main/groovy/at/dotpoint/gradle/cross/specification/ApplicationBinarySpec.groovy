package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.binary.BaseBinarySpec

/**
 * Created by RK on 19.03.16.
 */
class ApplicationBinarySpec extends BaseBinarySpec implements IApplicationBinarySpecInternal
{

	@Override
	void setTargetPlatform(IPlatform platform) {

	}

	@Override
	void setTargetFlavor(IFlavor releaseType) {

	}

	@Override
	IApplicationComponentSpec getApplication() {
		return null
	}

	@Override
	IPlatform getTargetPlatform() {
		return null
	}

	@Override
	IFlavor getTargetFlavor() {
		return null
	}

	@Override
	Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes() {
		return null
	}
}
