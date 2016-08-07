package at.dotpoint.gradle.cross.configuration.builder

import at.dotpoint.gradle.cross.configuration.model.Configuration
import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirement
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirementInternal;
import at.dotpoint.gradle.cross.configuration.requirement.command.IConfigurationCommand
/**
 * Created by RK on 16.05.2016.
 */
class ConfigurationBuilder implements IConfigurationBuilder
{
	//
    public static ConfigurationBuilder getInstance()
    {
        return new ConfigurationBuilder();
    }

	/**
	 *
	 * @param requirement
	 * @return
	 */
	@Override
	IConfiguration build( Iterable<IConfigurationRequirementInternal> requirements )
	{
		IConfiguration configuration = new Configuration();

		for( IConfigurationRequirementInternal requirement : requirements )
		{
			ArrayList<IConfigurationCommand> commands = requirement.getConfigurationCommands();

			for( IConfigurationCommand command : commands )
				configuration.add( command.setting );
		}

		return configuration;
	}
}
