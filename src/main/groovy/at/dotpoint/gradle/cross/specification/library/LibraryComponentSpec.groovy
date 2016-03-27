package at.dotpoint.gradle.cross.specification.library

import at.dotpoint.gradle.cross.specification.ApplicationComponentSpec
import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser
import at.dotpoint.gradle.cross.variant.parser.flavor.library.LibraryNotationParser
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement

/**
 * Created by RK on 20.03.16.
 */
class LibraryComponentSpec extends ApplicationComponentSpec	implements ILibraryComponentSpecInternal
{
	LibraryComponentSpec()
	{
		super( LibraryNotationParser.getInstance() );
	}
}
