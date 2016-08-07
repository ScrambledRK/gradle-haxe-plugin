package at.dotpoint.gradle.cross.configuration.builder

import at.dotpoint.gradle.cross.configuration.model.Configuration
import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirementInternal
import at.dotpoint.gradle.cross.configuration.requirement.command.IConfigurationCommand
import at.dotpoint.gradle.cross.configuration.setting.ConfigurationSetting
import at.dotpoint.gradle.cross.convention.ConventionUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.api.Project
/**
 * Created by RK on 16.05.2016.
 */
class ConfigurationBuilder implements IConfigurationBuilder
{
	//
	private final Project project;

	/**
	 *
	 * @param project
	 */
	ConfigurationBuilder( Project project )
	{
		this.project = project
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 *
	 * @param variantCombination
	 * @return
	 */
	IConfiguration build( VariantCombination<IVariant> variantCombination )
	{
		ArrayList<IConfigurationRequirementInternal> requirements = new ArrayList<>();

		for( Object variant : variantCombination )
		{
			if( variant instanceof IVariant )
				requirements.add( variant.configuration as IConfigurationRequirementInternal );
		}

		// ------------------------------- //

		IConfiguration configuration = new Configuration();

		for( IConfigurationRequirementInternal requirement : requirements )
		{
			ArrayList<IConfigurationCommand> commands = requirement.getConfigurationCommands();

			for( IConfigurationCommand command : commands )
			{
				String name = command.setting.name;
				Object value = command.setting.value;

				if( value instanceof String )
					value = value.replaceAll(/\@\{(\w+\.\w+)\}/){ m, k -> this.getStringVariable( variantCombination, k ) };

				configuration.add( new ConfigurationSetting( name, value ) );
			}
		}

		return configuration;
	}

	/**
	 *
	 * @param variable
	 * @return
	 */
	public String getStringVariable( VariantCombination<IVariant> variantCombination, String variable )
	{
		if( variable == "variation.buildDir" )
			return ConventionUtil.getVariationBuildDir( this.project, variantCombination ).path;

		return variable;
	}
}
