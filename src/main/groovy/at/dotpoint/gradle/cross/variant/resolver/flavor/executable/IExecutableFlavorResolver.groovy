package at.dotpoint.gradle.cross.variant.resolver.flavor.executable

import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.flavor.IFlavorResolver

/**
 * Created by RK on 11.03.16.
 */
interface IExecutableFlavorResolver extends IFlavorResolver<IExecutableFlavor, ExecutableFlavorRequirement> {

}