package at.dotpoint.gradle.cross.options.builder;

import at.dotpoint.gradle.cross.convention.ConventionUtil;
import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.model.Options;
import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirementInternal;
import at.dotpoint.gradle.cross.options.requirement.command.IOptionsCommand;
import at.dotpoint.gradle.cross.options.setting.OptionsSetting;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RK on 16.05.2016.
 */
public class OptionsBuilder implements IOptionsBuilder
{
	//
	private final File buildDir;

	/**
	 *
	 * @param project
	 */
	OptionsBuilder( File buildDir )
	{
		this.buildDir = buildDir
	}

	// ------------------------------------------------------------ //
	// ------------------------------------------------------------ //

	/**
	 *
	 * @param variantCombination
	 * @return
	 */
	public IOptions build( Iterable<IOptionsRequirementInternal> requirements )
	{
		IOptions configuration = new Options();

		for( IOptionsRequirementInternal requirement : requirements )
		{
			ArrayList<IOptionsCommand> commands = requirement.getConfigurationCommands();

			for( IOptionsCommand command : commands )
			{
				String name = command.setting.name;
				Object value = command.setting.value;

				if( value instanceof String )
					value = value.replaceAll(/\@\{(\w+\.\w+)\}/){ m, k -> this.getStringVariable( variantCombination, k ) }

				configuration.add( new OptionsSetting( name, value ) );
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
			return ConventionUtil.getVariationBuildDir( this.buildDir, variantCombination ).path;

		return variable;
	}
}
