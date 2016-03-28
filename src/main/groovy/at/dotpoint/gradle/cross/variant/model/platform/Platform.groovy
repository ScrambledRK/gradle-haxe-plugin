package at.dotpoint.gradle.cross.variant.model.platform

import at.dotpoint.gradle.cross.util.DefaultDisplayNamed
import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed

/**
 *
 */
@HasInternalProtocol
//
public interface IPlatform extends org.gradle.platform.base.Platform, IVariant {

}

/**
 *
 */
@Managed
//
public interface IPlatformInternal extends IPlatform {

}

/**
 * Created by RK on 11.03.16.
 */
class Platform extends DefaultDisplayNamed implements IPlatformInternal {


	public Platform(String name) {
			super(name, name)
		}

	public Platform(String name, String displayName) {
		super(name, displayName)
	}
}
