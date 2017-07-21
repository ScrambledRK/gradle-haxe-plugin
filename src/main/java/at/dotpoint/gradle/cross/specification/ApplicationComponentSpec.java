package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.dependency.container.CrossDependencySpecContainer;
import at.dotpoint.gradle.cross.dependency.container.IDependencySpecContainer;
import at.dotpoint.gradle.cross.options.requirement.IOptionsRequirement;
import at.dotpoint.gradle.cross.options.requirement.OptionsRequirement;
import at.dotpoint.gradle.cross.variant.parser.buildtype.BuildTypeNotationParser;
import at.dotpoint.gradle.cross.variant.parser.buildtype.IBuildTypeNotationParser;
import at.dotpoint.gradle.cross.variant.parser.flavor.FlavorNotationParser;
import at.dotpoint.gradle.cross.variant.parser.flavor.IFlavorNotationParser;
import at.dotpoint.gradle.cross.variant.parser.platform.IPlatformNotationParser;
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser;
import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;
import at.dotpoint.gradle.cross.variant.requirement.buildtype.IBuildTypeRequirement;
import at.dotpoint.gradle.cross.variant.requirement.flavor.IFlavorRequirement;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import org.gradle.model.ModelMap;
import org.gradle.model.internal.core.ModelMaps;
import org.gradle.model.internal.core.MutableModelNode;
import org.gradle.model.internal.type.ModelType;
import org.gradle.platform.base.component.BaseComponentSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by RK on 19.03.16.
 */
public class ApplicationComponentSpec extends BaseComponentSpec implements IApplicationComponentSpecInternal
{
	//
	private final MutableModelNode tests;
	private static final ModelType<ITestComponentSpec> TEST_COMPONENT_SPEC_MODEL_TYPE =
			ModelType.of(ITestComponentSpec.class);

	//
    protected final ArrayList<PlatformRequirement> targetPlatformList;
    protected final ArrayList<IFlavorRequirement> targetFlavorList;
    protected final ArrayList<IBuildTypeRequirement> targetBuildTypeList;

	//
	protected final IPlatformNotationParser platformNotationParser;
	protected final IFlavorNotationParser flavorNotationParser;
	protected final IBuildTypeNotationParser buildTypeNotationParser;

	//
	protected IOptionsRequirement configuration;
	private IDependencySpecContainer dependencies;

	// --------------------------------------------- //
	// --------------------------------------------- //

	/**
	 */
	public ApplicationComponentSpec()
	{
		this.targetPlatformList = new ArrayList<>();
		this.targetFlavorList = new ArrayList<>();
		this.targetBuildTypeList = new ArrayList<>();

		this.platformNotationParser = PlatformNotationParser.getInstance();
		this.flavorNotationParser = FlavorNotationParser.getInstance();
		this.buildTypeNotationParser = BuildTypeNotationParser.getInstance();

		this.tests = ModelMaps.addModelMapNode( getInfo().modelNode,
				TEST_COMPONENT_SPEC_MODEL_TYPE, "tests" );
	}

	// --------------------------------------------- //
	// --------------------------------------------- //

	@Override
    public ModelMap<ITestComponentSpec> getTests() {
        return ModelMaps.toView( this.tests, TEST_COMPONENT_SPEC_MODEL_TYPE );
    }

	@Override
	public IDependencySpecContainer getDependencies()
	{
		if( this.dependencies == null )
			this.dependencies = new CrossDependencySpecContainer();
		
		return dependencies;
	}
 
	// --------------------------------------------- //
	// --------------------------------------------- //

	/**
	 */
	@Override
	public List<PlatformRequirement> getTargetPlatforms()
	{
		return Collections.unmodifiableList( this.targetPlatformList );
	}

	/**
	 */
	@Override
	public List<IFlavorRequirement> getTargetFlavors()
	{
		return Collections.unmodifiableList( this.targetFlavorList );
	}

	/**
	 */
	@Override
	public List<IBuildTypeRequirement> getTargetBuildTypes()
	{
		return Collections.unmodifiableList( this.targetBuildTypeList );
	}

	/**
	 */
	@Override
	public List<List<? extends IVariantRequirement>> getVariantRequirements()
	{
		List<List<? extends IVariantRequirement>> variants = new ArrayList<>();
		variants.add( this.getTargetPlatforms() );
		variants.add( this.getTargetFlavors() );
		variants.add( this.getTargetBuildTypes() );

		return Collections.unmodifiableList( variants );
	}

	// -------------------------------------------------------------------------- //
	// -------------------------------------------------------------------------- //

	/**
	 *
	 */
	public IOptionsRequirement getOptions()
	{
		if( this.configuration == null )
			this.configuration = new OptionsRequirement();

		return this.configuration;
	}

	@Override
	public void setOptions( IOptionsRequirement configuration )
	{
		this.configuration = configuration;
	}

	// -------------------------------------------------------------------------- //
	// -------------------------------------------------------------------------- //

	/**
	 *
	 */
	@Override
	public void platform( Iterable<Object> platformRequirements )
	{
		for( Object platformRequirement : platformRequirements )
			this.platform( platformRequirement );
	}

	@Override
	public void platform( Object[] platformRequirements )
	{
		for( Object platformRequirement : platformRequirements )
			this.platform( platformRequirement );
	}

	@Override
	public void platform( Object platformRequirements )
	{
		PlatformRequirement requirement = this.platformNotationParser.parseNotation( platformRequirements );

		if( requirement != null )
			this.targetPlatformList.add( requirement );
	}

	/**
	 *
	 */
	@Override
	public void flavor( Iterable<Object> flavorRequirements )
	{
		for( Object flavorRequirement : flavorRequirements )
			this.flavor( flavorRequirement );
	}

	@Override
	public void flavor( Object[] flavorRequirements )
	{
		for( Object flavorRequirement : flavorRequirements )
			this.flavor( flavorRequirement );
	}

	@Override
	public void flavor( Object flavorRequirements )
	{
		IFlavorRequirement requirement = this.flavorNotationParser.parseNotation( flavorRequirements );

		if( requirement != null )
			this.targetFlavorList.add( requirement );
	}

	/**
	 *
	 */
	@Override
	public void buildType( Iterable<Object> buildTypeRequirements )
	{
		for( Object buildTypeRequirement : buildTypeRequirements )
			this.buildType( buildTypeRequirement );
	}

	@Override
	public void buildType( Object[] buildTypeRequirements )
	{
		for( Object buildTypeRequirement : buildTypeRequirements )
			this.buildType( buildTypeRequirement );
	}

	@Override
	public void buildType( Object buildTypeRequirements )
	{
		IBuildTypeRequirement requirement = this.buildTypeNotationParser.parseNotation( buildTypeRequirements );

		if( requirement != null )
			this.targetBuildTypeList.add( requirement );
	}
}
