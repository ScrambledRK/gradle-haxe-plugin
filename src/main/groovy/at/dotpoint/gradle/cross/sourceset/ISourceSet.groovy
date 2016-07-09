package at.dotpoint.gradle.cross.sourceset

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.target.IVariationsSource
import at.dotpoint.gradle.cross.variant.target.IVariationsSourceInternal
import org.gradle.internal.HasInternalProtocol
import org.gradle.language.base.DependentSourceSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.LanguageSourceSetInternal
import org.gradle.platform.base.TransformationFileType
/**
 * Created by RK on 27.03.2016.
 */
@HasInternalProtocol
//
public interface ISourceSet extends LanguageSourceSet, DependentSourceSet, TransformationFileType, IVariationsSource
{
	IPlatform getSourcePlatform();
	void platform( Object platformRequirements );
}

/**
 *
 */
public interface ISourceSetInternal extends ISourceSet, LanguageSourceSetInternal, IVariationsSourceInternal
{
	void setSourcePlatform( IPlatform platform );
	PlatformRequirement getPlatformRequirement();
}