package at.dotpoint.gradle.cross.variant.factory;

import at.dotpoint.gradle.cross.variant.model.IVariant;
import org.gradle.api.NamedDomainObjectFactory;

/**
 * Created by RK on 28.03.2016.
 */
public interface IVariantFactory<TVariant extends IVariant> extends NamedDomainObjectFactory<TVariant> {

}