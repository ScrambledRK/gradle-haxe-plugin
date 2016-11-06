package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.IVariationsTargetInternal;
import org.gradle.platform.base.internal.BinarySpecInternal;

/**
 * Created by RK on 11.03.16.
 */
//
public interface IApplicationBinarySpecInternal extends IApplicationBinarySpec, BinarySpecInternal, IVariationsTargetInternal
{
	//
	void setTargetPlatform( IPlatform platform );

	//
	void setTargetFlavor( IFlavor releaseType );

	//
	void setTargetBuildType( IBuildType releaseType );

	//
	void setTargetVariant( IVariant variant );

	//
	void setOptions( IOptions configuration );
}
