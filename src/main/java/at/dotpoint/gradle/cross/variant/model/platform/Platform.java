package at.dotpoint.gradle.cross.variant.model.platform;

import at.dotpoint.gradle.cross.variant.model.DefaultVariant;

import java.io.File;
import java.util.List;

/**
 * Created by RK on 11.03.16.
 */
public class Platform extends DefaultVariant implements IPlatformInternal
{
	
	private List<String> directories;
	
	private File mainSourceDirectory;
	private File unitSourceDirectory;
	
	// --------------------- //
	// --------------------- //
	
	//
	public Platform( String name )
	{
		this( name, name );
	}
	
	//
	public Platform( String name, String displayName )
	{
		super( name, displayName );
	}
	
	// --------------------- //
	// --------------------- //
	
	@Override
	public List<String> getDirectories()
	{
		return directories;
	}
	
	public void setDirectories( List<String> directories )
	{
		this.directories = directories;
	}
	
	@Override
	public File getMainSourceDirectory()
	{
		return this.mainSourceDirectory;
	}
	
	@Override
	public void setMainSourceDirectory( File directory )
	{
		this.mainSourceDirectory = directory;
	}
	
	@Override
	public File getUnitSourceDirectory()
	{
		return this.unitSourceDirectory;
	}
	
	@Override
	public void setUnitSourceDirectory( File directory )
	{
		this.unitSourceDirectory = directory;
	}
}
