package at.dotpoint.gradle.cross.sourceset

import at.dotpoint.gradle.cross.variant.model.platform.Platform
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.platform.base.DependencySpecContainer
import org.gradle.platform.base.internal.DefaultDependencySpecContainer


/**
 * Created by RK on 27.03.2016.
 */
public class CrossSourceSet extends BaseLanguageSourceSet implements ISourceSetInternal
{

	private Platform platform;
	private PlatformRequirement platformRequirement;

	private final DefaultDependencySpecContainer dependencies = new DefaultDependencySpecContainer();

    // --------------------------------------------------- //
	// --------------------------------------------------- //

	CrossSourceSet()
	{
		this( null, null );
	}

	CrossSourceSet(Platform platform, PlatformRequirement platformRequirement)
	{
		this.platform = platform
		this.platformRequirement = platformRequirement
	}

	// --------------------------------------------------- //
	// --------------------------------------------------- //

	@Override
	public void platform( Object platformRequirements )
	{
		PlatformRequirement requirement = PlatformNotationParser.getInstance().parseNotation( platformRequirements );

		if( requirement != null )
			this.platformRequirement = requirement;
	}

	PlatformRequirement getPlatformRequirement() {
			return platformRequirement
	}

	/**
	*
	*/
	void setTargetPlatform( Platform platform )
	{
		this.platform = platform;
	}

	Platform getTargetPlatform()
	{
		return this.platform;
	}

	@Override
	protected String getLanguageName() {

		if (this.platform != null)
			return this.platform.getName() + "Source";

		return super.getLanguageName()
	}

	// --------------------------------------------------- //
	// --------------------------------------------------- //

	@Override
    public DependencySpecContainer getDependencies() {
        return dependencies;
    }
}