package at.dotpoint.gradle.cross.sourceset;

import at.dotpoint.gradle.cross.dependency.container.IDependencySpecContainer;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.IVariationsSource;
import at.dotpoint.gradle.cross.variant.target.IVariationsTarget;
import org.gradle.internal.HasInternalProtocol;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.platform.base.TransformationFileType;
/**
 * Created by RK on 27.03.2016.
 */
@HasInternalProtocol
//
public interface ISourceSet extends LanguageSourceSet, TransformationFileType, IVariationsSource, IVariationsTarget
{
	//
	IDependencySpecContainer getDependencies();

	//
	IPlatform getSourcePlatform();
	IPlatform getTargetPlatform();

	//
	void sourcePlatform( Object platformRequirements );
	void targetPlatform( Object platformRequirements );
}

