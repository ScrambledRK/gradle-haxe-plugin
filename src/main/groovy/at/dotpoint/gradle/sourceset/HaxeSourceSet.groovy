package at.dotpoint.gradle.sourceset

import at.dotpoint.gradle.platform.HaxePlatform
import org.gradle.internal.HasInternalProtocol
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.LanguageSourceSetInternal
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.model.Managed

/**
 *
 */
@HasInternalProtocol
//
public interface HaxeSourceSet extends LanguageSourceSet
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
}
