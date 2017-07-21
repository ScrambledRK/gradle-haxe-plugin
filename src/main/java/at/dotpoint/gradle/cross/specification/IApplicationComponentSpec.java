package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement;
import org.gradle.model.ModelMap;
import org.gradle.platform.base.ApplicationSpec;
import org.gradle.platform.base.VariantComponentSpec;

/**
 *
 */
public interface IApplicationComponentSpec extends IGeneralComponentSpec, ApplicationSpec, VariantComponentSpec
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
	IOptionsRequirement getOptions();

	//
	ModelMap<ITestComponentSpec> getTests();
}

