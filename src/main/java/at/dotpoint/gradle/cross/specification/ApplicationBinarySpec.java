package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.nativeplatform.ObjectFile;
import org.gradle.platform.base.TransformationFileType;
import org.gradle.platform.base.binary.BaseBinarySpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 19.03.16.
 */
public class ApplicationBinarySpec extends BaseBinarySpec implements IApplicationBinarySpecInternal
{

	private IPlatform platform;
	private IFlavor flavor;
	private IBuildType buildType;

	private IOptions configuration;
	private List<File> buildResult;

	// ------------------------------------------------------------------ //
	// ------------------------------------------------------------------ //

	@Override
	public void setOptions( IOptions configuration )
	{
		this.configuration = configuration;
	}

	@Override
	public IOptions getOptions()
	{
		return this.configuration;
	}

	// -------------------------------------- //
	// -------------------------------------- //
	//

	@Override
	public void setBuildResult( List<File> buildResult )
	{
		this.buildResult = buildResult;
	}

	@Override
	public List<File> getBuildResult()
	{
		if( this.buildResult == null )
			this.buildResult = new ArrayList<>();

		return this.buildResult;
	}


	// -------------------------------------- //
	// -------------------------------------- //
	// Variants

	/**
	 *
	 * @param variant
	 */
	@Override
	public void setTargetVariant( IVariant variant )
	{
		if( variant instanceof IPlatform )
			this.setTargetPlatform( (IPlatform) variant );

		if( variant instanceof IFlavor )
			this.setTargetFlavor( (IFlavor) variant );

		if( variant instanceof IBuildType )
			this.setTargetBuildType( (IBuildType) variant );
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	public VariantCombination<IVariant> getTargetVariantCombination()
	{
		VariantCombination<IVariant> container = new VariantCombination<IVariant>();

		if( this.platform != null )
			container.add( this.platform );

		if( this.flavor != null )
			container.add( this.flavor );

		if( this.buildType != null )
			container.add( this.buildType );

		return container;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	public void setTargetVariantCombination( VariantCombination<IVariant> combination )
	{
		this.setTargetPlatform( combination.getVariant( IPlatform.class ) );
		this.setTargetFlavor( combination.getVariant( IFlavor.class ) );
		this.setTargetBuildType( combination.getVariant( IBuildType.class ) );
	}

	// -------------------------------------- //
	// -------------------------------------- //
	// IPlatform

	/**
	 *
	 * @param platform
	 */
	@Override
	public void setTargetPlatform(IPlatform platform) {
		this.platform = platform;
	}

	@Override
	public IPlatform getTargetPlatform() {
		return this.platform;
	}

	// -------------------------------------- //
	// -------------------------------------- //
	// IFlavor

	/**
	 *
	 * @param flavor
	 */
	@Override
	public void setTargetFlavor(IFlavor flavor) {
		this.flavor = flavor;
	}

	@Override
	public IFlavor getTargetFlavor() {
		return this.flavor;
	}

	// -------------------------------------- //
	// -------------------------------------- //
	// IBuildType

	/**
	 */
	@Override
	public void setTargetBuildType(IBuildType buildType) {
		this.buildType = buildType;
	}

	@Override
	public IBuildType getTargetBuildType() {
		return this.buildType;
	}

	// -------------------------------------- //
	// -------------------------------------- //

	/**
	 */
	@Override
	public IApplicationComponentSpec getApplication() {
		return getComponentAs(IApplicationComponentSpec.class);
	}

	/**
	 */
	@Override
	public Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes() {
		return Collections.singleton(ObjectFile.class);
	}

	@Override
	public boolean hasCodependentSources() {
		return true; //super.hasCodependentSources()
	}

	// -------------------------------------- //
	// -------------------------------------- //

	@Override
	public String getDisplayName()
	{
		IApplicationComponentSpec applicationComponentSpec = this.getApplication();

		if( applicationComponentSpec == null )
			return super.getDisplayName();

		return super.getTypeName() + " '" + applicationComponentSpec.getProjectPath() + ":" + super.getIdentifier().getPath()  + "'";
	}
}
