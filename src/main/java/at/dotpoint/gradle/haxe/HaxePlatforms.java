package at.dotpoint.gradle.haxe;

import at.dotpoint.gradle.cross.variant.model.platform.IPlatformInternal;
import at.dotpoint.gradle.cross.variant.model.platform.Platform;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class HaxePlatforms
{
	//
	public final IPlatformInternal PLATFORM_HAXE;
	public final IPlatformInternal PLATFORM_JAVASCRIPT;
	public final IPlatformInternal PLATFORM_NEKO;
	public final IPlatformInternal PLATFORM_JAVA;
	
	//
	public final List<IPlatformInternal> PLATFORMS;
	
	// --------------------- //
	// --------------------- //
	
	//
	private static HaxePlatforms instance;
	
	public static HaxePlatforms getInstance()
	{
		if( instance == null )
			instance = new HaxePlatforms();
		
		return instance;
	}
	
	// --------------------- //
	// --------------------- //
	
	/**
	 *
	 */
	private HaxePlatforms()
	{
		this.PLATFORM_HAXE = new Platform( "haxe" );
		this.PLATFORM_HAXE.setDirectories( Collections.singletonList( "haxe" ) );
		
		this.PLATFORM_JAVASCRIPT = new Platform( "javascript" );
		this.PLATFORM_JAVASCRIPT.setDirectories( Arrays.asList( "haxe", "javascript", "js" ) );
		
		this.PLATFORM_NEKO = new Platform( "neko" );
		this.PLATFORM_NEKO.setDirectories( Arrays.asList( "haxe", "neko", "sys" ) );
		
		this.PLATFORM_JAVA = new Platform( "java" );
		this.PLATFORM_JAVA.setDirectories( Arrays.asList( "haxe", "java", "sys" ) );
		
		this.PLATFORMS = Arrays.asList( PLATFORM_HAXE, PLATFORM_JAVA, PLATFORM_JAVASCRIPT, PLATFORM_NEKO );
	}
	
	public IPlatformInternal getPlatformByName( String name )
	{
		for( IPlatformInternal platform : this.PLATFORMS )
		{
			if( platform.getName() == name )
				return platform;
		}
		
		return null;
	}
}
