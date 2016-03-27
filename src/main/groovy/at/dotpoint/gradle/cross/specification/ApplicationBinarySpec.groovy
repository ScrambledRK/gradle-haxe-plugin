package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.language.base.LanguageSourceSet
import org.gradle.model.Managed
import org.gradle.model.ModelMap
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.binary.BaseBinarySpec

/**
 * Created by RK on 19.03.16.
 */
class ApplicationBinarySpec extends BaseBinarySpec implements IApplicationBinarySpecInternal
{

	private IPlatform platform;
	private IFlavor flavor;

	@Override
	void setTargetPlatform(IPlatform platform) {
		this.platform = platform;
	}

	@Override
	void setTargetFlavor(IFlavor falvor) {
		this.flavor = falvor;
	}

	@Override
	IApplicationComponentSpec getApplication() {
		return getComponentAs(IApplicationComponentSpec.class);
	}

	@Override
	IPlatform getTargetPlatform() {
		return this.platform;
	}

	@Override
	IFlavor getTargetFlavor() {
		return this.flavor;
	}

	@Override
	Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes() {
		return new HashSet<? extends Class<? extends TransformationFileType>>(Arrays.asList(LanguageSourceSet.class));
	}

}
