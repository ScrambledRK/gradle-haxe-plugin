package at.dotpoint.gradle.cross.specification.executable

import at.dotpoint.gradle.cross.specification.ApplicationComponentSpec
import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser
import at.dotpoint.gradle.cross.variant.parser.flavor.executable.ExecutableNotationParser
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement

/**
 * Created by RK on 20.03.16.
 */
class ExecutableComponentSpec extends ApplicationComponentSpec<ExecutableFlavorRequirement>
		implements IExecutableComponentSpecInternal
{
	ExecutableComponentSpec()
	{
		super( ExecutableNotationParser.getInstance() );
	}
}
