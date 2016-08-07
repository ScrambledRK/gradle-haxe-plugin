package at.dotpoint.gradle.cross.configuration.builder

import at.dotpoint.gradle.cross.configuration.model.IConfiguration
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
/**
 * Created by RK on 16.05.2016.
 */
interface IConfigurationBuilder
{

	/**
	 *
	 * @param requirement
	 * @return
	 */
	IConfiguration build( VariantCombination<IVariant> variantCombination );
}