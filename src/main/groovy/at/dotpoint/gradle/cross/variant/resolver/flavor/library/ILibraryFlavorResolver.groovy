package at.dotpoint.gradle.cross.variant.resolver.flavor.library

import at.dotpoint.gradle.cross.variant.model.flavor.executable.IExecutableFlavor
import at.dotpoint.gradle.cross.variant.model.flavor.library.ILibraryFlavor
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.resolver.flavor.IFlavorResolver

/**
 * Created by RK on 11.03.16.
 */
interface ILibraryFlavorResolver extends IFlavorResolver<ILibraryFlavor, LibraryFlavorRequirement> {

}