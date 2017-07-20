package at.dotpoint.gradle.haxe.task.neko;

import at.dotpoint.gradle.haxe.task.AHaxeTask;

import java.io.File;

/**
 * Created by RK on 2016-11-28.
 */
public class HaxeNekoTask extends AHaxeTask
{
	/**
	 */
	protected String getNativeDependencyOption( File artifact )
	{
		return null;
	}

	/**
	 */
	protected String getOutputCommandOption()
	{
		String output = this.getOutputDir().getAbsolutePath();
		String main = this.getMainClassPath();
		
		//
		if( main != null && main.length() > 0 )
		{
			main = main.substring( main.lastIndexOf( "." ) + 1 );
			output += "/" + main + ".n";
		}
		else
		{
			output += "/" + this.getOutputDir().getName() + ".n";
		}
		
		return "-neko \"" + output + "\"";
	}
}
