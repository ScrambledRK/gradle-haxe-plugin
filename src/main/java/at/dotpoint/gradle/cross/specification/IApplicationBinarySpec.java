package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.IVariationsTarget;
import org.gradle.internal.HasInternalProtocol;
import org.gradle.platform.base.ApplicationBinarySpec;
import org.gradle.platform.base.Variant;
import org.gradle.platform.base.internal.HasIntermediateOutputsComponentSpec;

import java.io.File;
import java.util.List;

/**
 * Created by RK on 11.03.16.
 */
@HasInternalProtocol
//
public interface IApplicationBinarySpec extends ApplicationBinarySpec, HasIntermediateOutputsComponentSpec, IVariationsTarget
{
	//
	@Variant
	IPlatform getTargetPlatform();

	//
	@Variant
	IFlavor getTargetFlavor();

	//
	@Variant
	IBuildType getTargetBuildType();

	//
	IOptions getOptions();

	//
	@Override
	IApplicationComponentSpec getApplication();

	//
	List<File> getBuildResult();
}

