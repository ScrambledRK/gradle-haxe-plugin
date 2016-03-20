package at.dotpoint.gradle.cross.specification.library

import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpecInternal
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed


/**
 * Created by RK on 20.03.16.
 */
@HasInternalProtocol
//
public interface ILibraryComponentSpec extends IApplicationComponentSpec {

}

/**
 *
 */
@Managed
//
public interface ILibraryComponentSpecInternal
		extends IApplicationComponentSpecInternal<LibraryFlavorRequirement>, ILibraryComponentSpec {

}