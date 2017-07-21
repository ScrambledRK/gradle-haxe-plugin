package at.dotpoint.gradle.cross.variant.model.platform;

import org.gradle.model.Managed;

import java.io.File;
import java.util.List;

/**
 *
 */
@Managed
//
public interface IPlatformInternal extends IPlatform
{
	void setDirectories( List<String> directories );
	
	void setMainSourceDirectory( File directory );
	void setUnitSourceDirectory( File directory );
}
