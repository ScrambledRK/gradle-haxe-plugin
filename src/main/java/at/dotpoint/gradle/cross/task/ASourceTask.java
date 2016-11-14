package at.dotpoint.gradle.cross.task;

import at.dotpoint.gradle.cross.convention.ConventionUtil;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.tasks.SourceTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 09.07.2016.
 */
public class ASourceTask extends SourceTask implements ISourceTask
{

	//
	private Set<File> dependencies;

	//
	private List<ISourceSet> sourceSets;

	//
	private VariantCombination<IVariant> sourceVariantCombination;

	//
	private VariantCombination<IVariant> targetVariantCombination;

	//
	private File outputDir;

	// ***************************************************************** //
	// ***************************************************************** //

	/**
	 */
	public List<ISourceSet> getSourceSets()
	{
		if( this.sourceSets == null )
			this.sourceSets = new ArrayList<>();

		return this.sourceSets;
	}

	/**
	 */
	public void source( List<ISourceSet> sourceSets )
	{
		sourceSets.forEach( this::source );
	}

	public void source( ISourceSet sourceSet )
	{
		this.sourceSets.add( sourceSet );
		this.getInputs().files( sourceSet.getSource().getSrcDirs() );
	}

	/**
	 */
	public Set<File> getDependencies()
	{
		if( this.dependencies == null )
			this.dependencies = new HashSet<>();

		return this.dependencies;
	}

	/**
	 */
	public void dependency( Set<File> dependencySet )
	{
		dependencySet.forEach( this::dependency );
	}

	public void dependency( File dependency )
	{
		this.dependencies.add( dependency );
		this.getInputs().files( dependency );
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
