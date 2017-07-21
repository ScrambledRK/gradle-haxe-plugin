package at.dotpoint.gradle.cross.sourceset;

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import at.dotpoint.gradle.cross.variant.target.IVariationsSourceInternal;
import at.dotpoint.gradle.cross.variant.target.IVariationsTargetInternal;
import org.gradle.language.base.internal.LanguageSourceSetInternal;

/**
 *
 */
public interface ISourceSetInternal extends ISourceSet, LanguageSourceSetInternal,
		IVariationsSourceInternal, IVariationsTargetInternal
{
	//
	void setSourcePlatform( IPlatform platform );
	void setTargetPlatform( IPlatform platform );

	//
	PlatformRequirement getSourcePlatformRequirement();
	PlatformRequirement getTargetPlatformRequirement();
}
