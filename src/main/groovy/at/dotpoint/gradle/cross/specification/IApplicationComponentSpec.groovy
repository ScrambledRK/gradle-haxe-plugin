package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.configuration.requirement.IConfigurationRequirement
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement
import at.dotpoint.gradle.cross.variant.requirement.buildtype.IBuildTypeRequirement
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.platform.base.ApplicationSpec
/**
 *
 */
public interface IApplicationComponentSpec extends IGeneralComponentSpec, ApplicationSpec
{
	//
	void platform( Object platformRequirements );
	void platform( Object[] platformRequirements );
	void platform( Iterable<Object> platformRequirements );

	//
	void flavor( Object flavorRequirements );
	void flavor( Object[] flavorRequirements );
	void flavor( Iterable<Object> flavorRequirements );

	//
	void buildType( Object buildTypeRequirements );
	void buildType( Object[] buildTypeRequirements );
	void buildType( Iterable<Object> buildTypeRequirements );

	//
	IConfigurationRequirement getConfiguration()
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
	List<IBuildTypeRequirement> getTargetBuildTypes();

	//
	List<List<IVariantRequirement>> getVariantRequirements();

	//
	void setConfiguration( IConfigurationRequirement configuration );
}