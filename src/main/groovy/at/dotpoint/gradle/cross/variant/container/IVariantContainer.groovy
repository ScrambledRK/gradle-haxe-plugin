package at.dotpoint.gradle.cross.variant.container

import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer

/**
 * Created by RK on 28.03.2016.
 */
interface IVariantContainer<TVariant extends IVariant> extends ExtensiblePolymorphicDomainObjectContainer<TVariant> {

}