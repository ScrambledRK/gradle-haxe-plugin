package at.dotpoint.gradle.cross.options.requirement;

import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand;

import java.util.ArrayList;

/**
 *
 */
public interface IOptionsRequirementInternal extends IOptionsRequirement
{
	/**
	 *
	 * @return
	 */
	ArrayList<IOptionsCommand> getConfigurationCommands();
}
