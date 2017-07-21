package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.dependency.container.IDependencySpecContainer;
import org.gradle.internal.HasInternalProtocol;
import org.gradle.platform.base.ComponentSpec;
import org.gradle.platform.base.SourceComponentSpec;

/**
 * Created by RK on 11.03.16.
 */
@HasInternalProtocol
//
public interface IGeneralComponentSpec extends ComponentSpec, SourceComponentSpec
{
	//
	IDependencySpecContainer getDependencies();
}

