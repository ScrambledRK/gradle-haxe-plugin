package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.convention.ConventionUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import org.gradle.api.tasks.SourceTask
/**
 * Created by RK on 09.07.2016.
 */
class ACrossSourceTask extends SourceTask implements ICrossSourceTask
{

	//
	private VariantCombination<IVariant> sourceVariantCombination;

	//
	private VariantCombination<IVariant> targetVariantCombination;

	//
	private File outputDir;

	// ------------------------------------------------------------------- //
	// ------------------------------------------------------------------- //

	/**
	 *
	 */
	public ACrossSourceTask()
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
	public File getOutputDir()
	{
		if( this.outputDir == null )
			this.outputDir = ConventionUtil.getVariationBuildDir( this.project.buildDir, this.targetVariantCombination );

		return this.outputDir;
	}

	public void setOutputDir( File outputDir )
	{
		this.outputDir = outputDir;
	}
}
