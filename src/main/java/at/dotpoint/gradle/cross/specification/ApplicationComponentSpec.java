package at.dotpoint.gradle.cross.specification;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RK on 19.03.16.
 */
public class ApplicationComponentSpec extends GeneralComponentSpec implements IApplicationComponentSpecInternal
{
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
	 * @return
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
	 * @param platformRequirements
	 */
	@Override
	public void platform( Iterable<Object> platformRequirements )
	{
		Iterator<Object> iterator = platformRequirements.iterator();

		while( iterator.hasNext() )
			this.platform( iterator.next() );
	}

	@Override
	public void platform( Object[] platformRequirements )
	{
		for (int i = 0; i < platformRequirements.length; i++)
			this.platform( platformRequirements[i] );
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
	 * @param flavorRequirements
	 */
	@Override
	public void flavor( Iterable<Object> flavorRequirements )
	{
		Iterator<Object> iterator = flavorRequirements.iterator();

		while( iterator.hasNext() )
			this.flavor( iterator.next() );
	}

	@Override
	public void flavor( Object[] flavorRequirements )
	{
		for (int i = 0; i < flavorRequirements.length; i++)
			this.flavor( flavorRequirements[i] );
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
	 * @param buildTypeRequirements
	 */
	@Override
	public void buildType( Iterable<Object> buildTypeRequirements )
	{
		Iterator<Object> iterator = buildTypeRequirements.iterator();

		while( iterator.hasNext() )
			this.buildType( iterator.next() );
	}

	@Override
	public void buildType( Object[] buildTypeRequirements )
	{
		for (int i = 0; i < buildTypeRequirements.length; i++)
			this.buildType( buildTypeRequirements[i] );
	}

	@Override
	public void buildType( Object buildTypeRequirements )
	{
		IBuildTypeRequirement requirement = this.buildTypeNotationParser.parseNotation( buildTypeRequirements );

		if( requirement != null )
			this.targetBuildTypeList.add( requirement );
	}
}
