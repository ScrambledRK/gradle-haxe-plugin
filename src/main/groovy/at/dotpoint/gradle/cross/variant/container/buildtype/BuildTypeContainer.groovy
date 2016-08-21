package at.dotpoint.gradle.cross.variant.container.buildtype

import at.dotpoint.gradle.cross.variant.container.VariantContainer
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType
import org.gradle.internal.reflect.Instantiator
/**
 * Created by RK on 28.03.2016.
 */
class BuildTypeContainer extends VariantContainer<IBuildType> implements IBuildTypeContainer {

	BuildTypeContainer( Instantiator instantiator )
	{
		super( IBuildType.class, instantiator );
	}
}
