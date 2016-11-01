package at.dotpoint.gradle.haxe.specification;

import at.dotpoint.gradle.cross.specification.ApplicationComponentSpec;
import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser;
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement;

/**
 * Created by RK on 28.03.2016.
 */
public class HaxeComponentSpec extends ApplicationComponentSpec implements IHaxeComponentSpecInternal
{

	/**
	 *
	 * @param flavorNotationParser
	 */
	HaxeComponentSpec( IFlavorNotationParser<IFlavorRequirement> flavorNotationParser )
	{
		super( flavorNotationParser )
	}
}
