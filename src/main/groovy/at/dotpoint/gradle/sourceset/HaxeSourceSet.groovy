package at.dotpoint.gradle.sourceset

import at.dotpoint.gradle.platform.HaxePlatform
import org.gradle.internal.HasInternalProtocol
import org.gradle.language.base.DependentSourceSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.LanguageSourceSetInternal
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.model.Managed
import org.gradle.platform.base.DependencySpecContainer
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.internal.DefaultDependencySpecContainer

/**
 *
 */
@HasInternalProtocol
//
public interface HaxeSourceSet extends LanguageSourceSet, DependentSourceSet, TransformationFileType
{
	HaxePlatform getTargetPlatform();
}

/**
 *
 */
@Managed
//
public interface HaxeSourceSetInternal extends HaxeSourceSet, LanguageSourceSetInternal
{
	void setTargetPlatform( HaxePlatform platform );
}

/**
 *
 */
public class DefaultHaxeSourceSet extends BaseLanguageSourceSet implements HaxeSourceSetInternal
{

	private HaxePlatform platform;
	private final DefaultDependencySpecContainer dependencies = new DefaultDependencySpecContainer();

    // --------------------------------------------------- //
	// --------------------------------------------------- //

	/**
	*
	*/
	void setTargetPlatform( HaxePlatform platform )
	{
		this.platform = platform;
	}

	HaxePlatform getTargetPlatform()
	{
		return this.platform;
	}

	@Override
    public DependencySpecContainer getDependencies() {
        return dependencies;
    }
}
