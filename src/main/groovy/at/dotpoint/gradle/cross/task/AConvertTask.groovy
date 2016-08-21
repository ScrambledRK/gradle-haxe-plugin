package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.convention.ConventionUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.api.tasks.SourceTask
/**
 * Created by RK on 09.07.2016.
 */
class AConvertTask extends SourceTask implements IConvertTask
{

	//
	private VariantCombination<IVariant> sourceVariantCombination;

	//
	private VariantCombination<IVariant> targetVariantCombination;

	// ------------------------------------------------------------------- //
	// ------------------------------------------------------------------- //

	/**
	 *
	 */
	public AConvertTask()
	{
		super();
	}

	// ------------------------------------------------------------------- //
	// ------------------------------------------------------------------- //

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	void setSourceVariantCombination( VariantCombination<IVariant> combination )
	{
		this.sourceVariantCombination = combination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	VariantCombination<IVariant> getSourceVariantCombination()
	{
		if( this.sourceVariantCombination == null )
			this.setSourceVariantCombination( new VariantCombination<IVariant>() );

		return this.sourceVariantCombination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	void setTargetVariantCombination( VariantCombination<IVariant> combination )
	{
		this.targetVariantCombination = combination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	VariantCombination<IVariant> getTargetVariantCombination()
	{
		if( this.targetVariantCombination == null )
			this.setTargetVariantCombination( new VariantCombination<IVariant>() );

		return this.targetVariantCombination;
	}

	// ------------------------------------------------------------------- //
	// ------------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	protected File getOutputDir()
	{
		return ConventionUtil.getVariationBuildDir( this.project.buildDir, this.targetVariantCombination );
	}
}
