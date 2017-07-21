package at.dotpoint.gradle.cross.task;

import at.dotpoint.gradle.cross.convention.ConventionUtil;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.tasks.OutputDirectory;
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
	private List<File> sourceDirectories;

	//
	private VariantCombination<IVariant> sourceVariantCombination;

	//
	private VariantCombination<IVariant> targetVariantCombination;

	//
	private File outputDir;

	// ***************************************************************** //
	// ***************************************************************** //

	/**
	 * library dependencies and application/binary sources
	 */
	//
	public List<File> getSourceDirectories()
	{
		if( this.sourceDirectories == null )
			this.sourceDirectories = new ArrayList<>();

		return this.sourceDirectories;
	}

	/**
	 * library dependencies and application/binary sources
	 */
	public void source( List<ISourceSet> sourceSets )
	{
		sourceSets.forEach( this::source );
	}

	public void source( ISourceSet sourceSet )
	{
		IPlatform taskTargetPlatform = this.getTargetVariantCombination().getVariant( IPlatform.class );
		IPlatform sourceTargetPlatform = sourceSet.getTargetPlatform();

		if( sourceTargetPlatform != null && sourceTargetPlatform.getName() != taskTargetPlatform.getName() )
			return;
		
		//
		for( File dir : sourceSet.getSource().getSrcDirs() )
		{
			this.getSourceDirectories().add( dir );
			this.source( dir );
		}
	}

	/**
	 * module dependencies (artifacts)
	 */
	//
	public Set<File> getDependencies()
	{
		if( this.dependencies == null )
			this.dependencies = new HashSet<>();

		return this.dependencies;
	}

	/**
	 * module dependencies (artifacts)
	 */
	public void dependency( Set<File> dependencySet )
	{
		dependencySet.forEach( this::dependency );
	}

	public void dependency( File dependency )
	{
		this.getDependencies().add( dependency );
		this.source( dependency );
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
			this.setSourceVariantCombination( new VariantCombination<>() );

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
			this.setTargetVariantCombination( new VariantCombination<>() );

		return this.targetVariantCombination;
	}

	// ------------------------------------------------------------------- //
	// ------------------------------------------------------------------- //

	/**
	 */
	@OutputDirectory
	//
	public File getOutputDir()
	{
		if( this.outputDir == null )
			this.outputDir = ConventionUtil.getVariationBuildDir( this.getProject().getProjectDir(),
					this.getTargetVariantCombination() );

		return this.outputDir;
	}

	public void setOutputDir( File outputDir )
	{
		this.outputDir = outputDir;
	}
}
