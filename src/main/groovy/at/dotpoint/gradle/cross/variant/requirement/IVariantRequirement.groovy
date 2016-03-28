package at.dotpoint.gradle.cross.variant.requirement

import at.dotpoint.gradle.cross.variant.model.IVariant
import org.gradle.api.Named

/**
 * Created by RK on 11.03.16.
 */
public interface IVariantRequirement extends Named {

	//
	Class<? extends IVariant> getVariantType();
}