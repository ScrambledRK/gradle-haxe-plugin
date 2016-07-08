package at.dotpoint.gradle.cross.specification

import at.dotpoint.gradle.cross.variant.target.VariantCombination
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform
import org.gradle.language.base.LanguageSourceSet
import org.gradle.platform.base.TransformationFileType
import org.gradle.platform.base.binary.BaseBinarySpec

/**
 * Created by RK on 19.03.16.
 */
// TODO: refactor to simplify IVariationsTargetInternal methods
//
class ApplicationBinarySpec extends BaseBinarySpec implements IApplicationBinarySpecInternal
{

	private IPlatform platform;
	private IFlavor flavor;

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

		return container;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	void setTargetVariantCombination( VariantCombination<IVariant> combination )
	{
		this.setTargetPlatform( combination.getVariant( IPlatform.class ) );
		this.setTargetFlavor( combination.getVariant( IFlavor.class ) );
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

}
