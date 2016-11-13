package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;
import at.dotpoint.gradle.cross.variant.requirement.buildtype.IBuildTypeRequirement;
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;

import java.util.List;

/**
 *
 */
public interface IApplicationComponentSpecInternal extends IApplicationComponentSpec
{
	//
    List<PlatformRequirement> getTargetPlatforms();

	//
	List<IFlavorRequirement> getTargetFlavors();

	//
	List<IBuildTypeRequirement> getTargetBuildTypes();

	//
	List<List<? extends IVariantRequirement>> getVariantRequirements();

	//
	void setOptions( IOptionsRequirement configuration );

}
