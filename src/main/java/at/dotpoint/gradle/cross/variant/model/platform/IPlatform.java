package at.dotpoint.gradle.cross.variant.model.platform;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import org.gradle.internal.HasInternalProtocol;

import java.io.File;
import java.util.List;

/**
 *
 */
@HasInternalProtocol
//
public interface IPlatform extends org.gradle.platform.base.Platform, IVariant
{
	List<String> getDirectories();
	
	File getMainSourceDirectory();
	File getUnitSourceDirectory();
}


