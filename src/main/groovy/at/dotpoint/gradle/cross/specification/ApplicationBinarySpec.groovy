package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.options.model.IOptions
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.language.base.LanguageSourceSet
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.binary.BaseBinarySpec
/**
 * Created by RK on 19.03.16.
 */
class ApplicationBinarySpec extends BaseBinarySpec implements IApplicationBinarySpecInternal
{

	private IPlatform platform;
	private IFlavor flavor;
	private IBuildType buildType;

	private IOptions configuration;

	private IApplicationBinarySpec testBinarySpecTarget;
	private IApplicationBinarySpec testBinarySpecSource;

	// ------------------------------------------------------------------ //
	// ------------------------------------------------------------------ //

	@Override
	void setOptions( IOptions configuration )
	{
		this.configuration = configuration;
	}

	@Override
	IOptions getOptions()
	{
		return this.configuration;
	}

	// -------------------------------------- //
	// -------------------------------------- //
	// Variants

	/**
	 *
	 * @param variant
	 */
	@Override
	void setTargetVariant( IVariant variant )
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
	VariantCombination<IVariant> getTargetVariantCombination()
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
	void setTargetVariantCombination( VariantCombination<IVariant> combination )
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
	void setTargetPlatform(IPlatform platform) {
		this.platform = platform;
	}

	@Override
	IPlatform getTargetPlatform() {
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
	void setTargetFlavor(IFlavor flavor) {
		this.flavor = flavor;
	}

	@Override
	IFlavor getTargetFlavor() {
		return this.flavor;
	}

	// -------------------------------------- //
	// -------------------------------------- //
	// IBuildType

	/**
	 *
	 * @param flavor
	 */
	@Override
	void setTargetBuildType(IBuildType buildType) {
		this.buildType = buildType;
	}

	@Override
	IBuildType getTargetBuildType() {
		return this.buildType;
	}

	// -------------------------------------- //
	// -------------------------------------- //

	/**
	 *
	 * @return
	 */
	@Override
	IApplicationComponentSpec getApplication() {
		return getComponentAs(IApplicationComponentSpec.class);
	}

	/**
	 *
	 * @return
	 */
	@Override
	Set<? extends Class<? extends TransformationFileType>> getIntermediateTypes() {
		return new HashSet<? extends Class<? extends TransformationFileType>>(Arrays.asList(LanguageSourceSet.class));
	}

	@Override
	boolean hasCodependentSources() {
		return true; //super.hasCodependentSources()
	}

	// -------------------------------------- //
	// -------------------------------------- //

	@Override
	String getDisplayName()
	{
		IApplicationComponentSpec applicationComponentSpec = this.getApplication();

		if( applicationComponentSpec == null )
			return super.getDisplayName();

		return super.getTypeName() + " '" + applicationComponentSpec.projectPath + ":" + super.identifier.getPath()  + "'";
	}
}
