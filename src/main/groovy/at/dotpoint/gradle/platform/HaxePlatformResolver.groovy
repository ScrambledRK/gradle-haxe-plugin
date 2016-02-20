package at.dotpoint.gradle.platform

import org.codehaus.groovy.runtime.StackTraceUtils
import org.gradle.platform.base.Platform
import org.gradle.platform.base.PlatformContainer
import org.gradle.platform.base.internal.DefaultPlatformResolvers
import org.gradle.platform.base.internal.PlatformRequirement
import org.gradle.platform.base.internal.PlatformResolver

/**
 * Created by RK on 19.02.16.
 */
public class HaxePlatformResolver implements PlatformResolver<HaxePlatform>
{

	//
	private final PlatformContainer platformContainer;

	//
	public HaxePlatformResolver(PlatformContainer platformContainer)
	{
		this.platformContainer = platformContainer;
	}

	@Override
	Class<HaxePlatform> getType()
	{
		return HaxePlatform.class;
	}

	@Override
	HaxePlatform resolve( PlatformRequirement platformRequirement )
	{
		for( Platform platform : this.platformContainer )
		{
			if( platform.name.equals( platformRequirement.platformName ) )
				return platform;
		}

		return new DefaultHaxePlatform( platformRequirement.getPlatformName() );
	}

}