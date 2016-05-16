package at.dotpoint.gradle.cross.variant.factory.flavor

import at.dotpoint.gradle.cross.variant.factory.IVariantFactory
import at.dotpoint.gradle.cross.variant.model.flavor.executable.ExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor

/**
 * Created by RK on 28.03.2016.
 */
class ExecutableFlavorFactory implements IVariantFactory<IExecutableFlavor>
{
	@Override
	IExecutableFlavor create( String s )
	{
		return new ExecutableFlavor( s );
	}
}
