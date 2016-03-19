package at.dotpoint.gradle.cross.variant.resolver.platform

import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import at.dotpoint.gradle.cross.variant.resolver.IVariantResolver

/**
 * Created by RK on 11.03.16.
 */
interface IPlatformResolver extends IVariantResolver<IPlatform, PlatformRequirement> {

}
