package at.dotpoint.gradle.cross.task;

import at.dotpoint.gradle.cross.convention.ConventionUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.tasks.SourceTask;

import java.io.File;

/**
 * Created by RK on 09.07.2016.
 */
public class ACrossSourceTask extends SourceTask implements ICrossSourceTask
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
	public void setSourceVariantCombination( VariantCombination<IVariant> combination )
	{
		this.sourceVariantCombination = combination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public VariantCombination<IVariant> getSourceVariantCombination()
	{
		if( this.sourceVariantCombination == null )
			this.setSourceVariantCombination( new VariantCombination<IVariant>() );

		return this.sourceVariantCombination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public void setTargetVariantCombination( VariantCombination<IVariant> combination )
	{
		this.targetVariantCombination = combination;
	}

	/**
	 * IFlavor, IPlatform, IBuildType
	 */
	@Override
	public VariantCombination<IVariant> getTargetVariantCombination()
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
			this.outputDir = ConventionUtil.getVariationBuildDir( this.getProject().getProjectDir(), this.getTargetVariantCombination() );

		return this.outputDir;
	}

	public void setOutputDir( File outputDir )
	{
		this.outputDir = outputDir;
	}
}
