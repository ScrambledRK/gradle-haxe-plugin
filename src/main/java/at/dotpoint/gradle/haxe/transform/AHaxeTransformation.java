package at.dotpoint.gradle.haxe.transform;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransformation;
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformationData;
import at.dotpoint.gradle.cross.util.BinarySpecUtil;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import at.dotpoint.gradle.haxe.task.AHaxeTask;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 2016-11-28.
 */
abstract public class AHaxeTransformation extends ALifeCycleTransformation
{
	//
	public AHaxeTransformation( ServiceRegistry serviceRegistry )
	{
		super( serviceRegistry );
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 * SourceSet to convert
	 */
	@Override
	public boolean canTransform( IApplicationBinarySpec binarySpec )
	{
		boolean validSources = this.isValidSourceSets( BinarySpecUtil.getSourceSetList( binarySpec ) );
		boolean validVariation = this.isValidTargetVariation( binarySpec.getTargetVariantCombination() );

		return validSources && validVariation;
	}

	/**
	 */
	protected boolean isValidSourceSets( List<ISourceSet> sourceSets )
	{
		for( ISourceSet source : sourceSets )
		{
			if( !"haxe".equals( source.getSourcePlatform().getName() ) )
				return false;
		}

		return true;
	}

	/**
	 */
	protected boolean isValidTargetVariation( VariantCombination<IVariant> targetVariation )
	{
		return targetVariation != null;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 * Convert
	 */
	@Override
	protected List<Task> createConvertTransformation( ILifeCycleTransformationData target )
	{
		return this.createConvertTransformation( target.getBinarySpec(),
				NameUtil.getBinaryTaskName( target.getBinarySpec(), "convert" ) );
	}

	/**
	 * Compile
	 */
	@Override
	protected List<Task> createCompileTransformation( ILifeCycleTransformationData target )
	{
		return this.createCompileTransformation( target.getBinarySpec(),
				NameUtil.getBinaryTaskName( target.getBinarySpec(), "compile" ) );
	}

	/**
	 * TEST
	 */
	@Override
	protected List<Task> createTestTransformation( ILifeCycleTransformationData target, ITestComponentSpec testSpec )
	{
		IApplicationBinarySpec binarySpec = target.getBinarySpec();
		String name = testSpec.getName();

		List<Task> convert = this.createConvertTransformation( binarySpec, name );
		List<Task> compile = this.createCompileTransformation( binarySpec, name );

		// ------------------------------------------- //

		Task execute = this.createTestTask( binarySpec, testSpec );

		if( convert != null && !convert.isEmpty() && compile != null && !compile.isEmpty() )
			compile.get( compile.size() - 1 ).dependsOn( convert.get( convert.size() - 1 ) );

		if( compile != null && !compile.isEmpty() )
			execute.dependsOn( compile );

		// ------------------------------------------- //

		List<Task> result = new ArrayList<>();

		if( convert != null )
			result.addAll( convert );

		if( compile != null )
			result.addAll( compile );

		result.add( execute );

		return result;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	//
	abstract protected List<Task> createConvertTransformation( IApplicationBinarySpec binarySpec, String name );

	//
	abstract protected List<Task> createCompileTransformation( IApplicationBinarySpec binarySpec, String name );

	//
	protected abstract Task createTestTask( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec );

	// ************************************************************************************* //
	// ************************************************************************************* //
	// HaxeTask:

	/**
	 */
	protected AHaxeTask createHaxeTask( IApplicationBinarySpec binarySpec,
	                                    Class<? extends AHaxeTask> type, String postFix )
	{
		String name = NameUtil.getBinaryTaskName( binarySpec, "haxe", postFix );
		File output = this.getOutputDirectory( binarySpec, "haxe" );

		//
		return TaskUtil.createTask( binarySpec, type, name, it ->
		{
			it.setTargetVariantCombination( binarySpec.getTargetVariantCombination() );
			it.setOptions( binarySpec.getOptions() );
			it.setOutputDir( output );
		} );
	}

}
