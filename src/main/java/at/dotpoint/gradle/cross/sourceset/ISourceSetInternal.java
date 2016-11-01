package at.dotpoint.gradle.cross.sourceset;

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import at.dotpoint.gradle.cross.variant.target.IVariationsSourceInternal;
import org.gradle.language.base.internal.LanguageSourceSetInternal;

/**
 *
 */
public interface ISourceSetInternal extends ISourceSet, LanguageSourceSetInternal, IVariationsSourceInternal
{
	//
	void setSourcePlatform( IPlatform platform );

	//
	PlatformRequirement getPlatformRequirement();
}
