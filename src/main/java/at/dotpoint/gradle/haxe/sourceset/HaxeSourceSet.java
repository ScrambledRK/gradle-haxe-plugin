package at.dotpoint.gradle.haxe.sourceset;

import at.dotpoint.gradle.cross.sourceset.CrossSourceSet;
import at.dotpoint.gradle.cross.variant.model.platform.Platform;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;

/**
 * Created by RK on 28.03.2016.
 */
public class HaxeSourceSet extends CrossSourceSet implements IHaxeSourceSetInternal
{
	public HaxeSourceSet()
	{
		super( new Platform("haxe"), new PlatformRequirement( "haxe" ) );
	}
}
