package at.dotpoint.gradle.cross.variant.container

import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.api.internal.DefaultPolymorphicDomainObjectContainer
import org.gradle.internal.reflect.Instantiator

/**
 * Created by RK on 28.03.2016.
 */
class VariantContainer<TVariant extends IVariant> extends DefaultPolymorphicDomainObjectContainer<TVariant>
		implements IVariantContainer<TVariant>
{
	public VariantContainer( Class<TVariant> type, Instantiator instantiator )
	{
		super( type, instantiator );
	}
}
