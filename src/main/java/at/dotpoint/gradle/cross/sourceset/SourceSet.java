package at.dotpoint.gradle.cross.sourceset;

import at.dotpoint.gradle.cross.dependency.container.CrossDependencySpecContainer;
import at.dotpoint.gradle.cross.dependency.container.IDependencySpecContainer;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.parser.platform.PlatformNotationParser;
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.language.base.sources.BaseLanguageSourceSet;
import org.gradle.model.Managed;

/**
 * Created by RK on 27.03.2016.
 */
@Managed
//
public class SourceSet extends BaseLanguageSourceSet implements ISourceSetInternal
{
	
	private IPlatform sourcePlatform;
	private IPlatform targetPlatform;
	
	private PlatformRequirement sourcePlatformRequirement;
	private PlatformRequirement targetPlatformRequirement;
	
	private IDependencySpecContainer dependencies;
	
	// --------------------------------------------------- //
	// --------------------------------------------------- //
	
	public SourceSet()
	{
		this( null, null );
	}
	
	public SourceSet( IPlatform sourcePlatform, PlatformRequirement sourcePlatformRequirement )
	{
		this.sourcePlatform = sourcePlatform;
		this.sourcePlatformRequirement = sourcePlatformRequirement;
	}
	
	// --------------------------------------------------- //
	// --------------------------------------------------- //
	// source platform
	
	//
	public void setSourcePlatform( IPlatform platform )
	{
		this.sourcePlatform = platform;
	}
	
	//
	public IPlatform getSourcePlatform()
	{
		return this.sourcePlatform;
	}
	
	//
	public PlatformRequirement getSourcePlatformRequirement()
	{
		return sourcePlatformRequirement;
	}
	
	/**
	 *
	 */
	@Override
	public void sourcePlatform( Object platformRequirements )
	{
		PlatformRequirement requirement = PlatformNotationParser.getInstance().parseNotation( platformRequirements );
		
		if( requirement != null )
			this.sourcePlatformRequirement = requirement;
	}
	
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public void setSourceVariantCombination( VariantCombination<IVariant> combination )
	{
		this.sourcePlatform = combination.getVariant( IPlatform.class );
	}
	
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public VariantCombination<IVariant> getSourceVariantCombination()
	{
		VariantCombination<IVariant> container = new VariantCombination<IVariant>();
		
		if( this.sourcePlatform != null )
			container.add( this.sourcePlatform );
		
		return container;
	}
	
	// --------------------------------------------------- //
	// --------------------------------------------------- //
	// target platform
	
	//
	public void setTargetPlatform( IPlatform platform )
	{
		this.targetPlatform = platform;
	}
	
	//
	public IPlatform getTargetPlatform()
	{
		return this.targetPlatform;
	}
	
	//
	public PlatformRequirement getTargetPlatformRequirement()
	{
		return targetPlatformRequirement;
	}
	
	/**
	 *
	 */
	@Override
	public void targetPlatform( Object platformRequirements )
	{
		PlatformRequirement requirement = PlatformNotationParser.getInstance().parseNotation( platformRequirements );
		
		if( requirement != null )
			this.targetPlatformRequirement = requirement;
	}
	
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public void setTargetVariantCombination( VariantCombination<IVariant> combination )
	{
		this.targetPlatform = combination.getVariant( IPlatform.class );
	}
	
	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public VariantCombination<IVariant> getTargetVariantCombination()
	{
		VariantCombination<IVariant> container = new VariantCombination<IVariant>();
		
		if( this.targetPlatform != null )
			container.add( this.targetPlatform );
		
		return container;
	}
	
	// --------------------------------------------------- //
	// --------------------------------------------------- //
	
	@Override
	public IDependencySpecContainer getDependencies()
	{
		if( this.dependencies == null )
			this.dependencies = new CrossDependencySpecContainer();
		
		return dependencies;
	}
	
	@Override
	protected String getLanguageName()
	{
		
		if( this.sourcePlatform != null )
			return this.sourcePlatform.getName() + "Source";
		
		return super.getLanguageName();
	}
}