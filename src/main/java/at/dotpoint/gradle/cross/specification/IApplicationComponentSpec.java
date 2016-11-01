package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;
import at.dotpoint.gradle.cross.variant.requirement.buildtype.IBuildTypeRequirement;
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import org.gradle.platform.base.ApplicationSpec;

import java.util.List;

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
	IOptionsRequirement getOptions()
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
	void setOptions( IOptionsRequirement configuration );
}