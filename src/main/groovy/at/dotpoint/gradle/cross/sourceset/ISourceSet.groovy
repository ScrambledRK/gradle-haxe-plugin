package at.dotpoint.gradle.cross.sourceset

import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.internal.HasInternalProtocol
import org.gradle.language.base.DependentSourceSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.LanguageSourceSetInternal
import org.gradle.model.Managed
import org.gradle.platform.base.TransformationFileType


/**
 * Created by RK on 27.03.2016.
 */
@HasInternalProtocol
//
public interface ISourceSet extends LanguageSourceSet, DependentSourceSet, TransformationFileType
{
	Platform getTargetPlatform();
	void platform( Object platformRequirements );
}

/**
 *
 */
public interface ISourceSetInternal extends ISourceSet, LanguageSourceSetInternal
{
	void setTargetPlatform( Platform platform );
	PlatformRequirement getPlatformRequirement();
}