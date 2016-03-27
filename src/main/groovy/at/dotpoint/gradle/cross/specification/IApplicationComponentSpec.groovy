package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.ApplicationSpec

/**
 *
 */
public interface IApplicationComponentSpec extends IGeneralComponentSpec, ApplicationSpec
{
	//
	void platform( Object platformRequirements );

	//
	void flavor( Object flavorRequirements );
}

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
	List<List<IVariantRequirement>> getVariantRequirements();
}