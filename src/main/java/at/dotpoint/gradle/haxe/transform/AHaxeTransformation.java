package at.dotpoint.gradle.haxe.transform;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.setting.OptionsSetting;
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
		return this.createConvertTransformation( target.getBinarySpec(), target.getBinarySpec().getOptions(),
				"convert", "binary" );
	}

	/**
	 * Compile
	 */
	@Override
	protected List<Task> createCompileTransformation( ILifeCycleTransformationData target )
	{
		return this.createCompileTransformation( target.getBinarySpec(),  target.getBinarySpec().getOptions(),
				"compile", "binary" );
	}

	/**
	 * TEST
	 */
	@Override
	protected List<Task> createTestTransformation( ILifeCycleTransformationData target, ITestComponentSpec testSpec )
	{
		IApplicationBinarySpec binarySpec = target.getBinarySpec();
		String name = testSpec.getName();

		IOptions options = binarySpec.getOptions().clone();
		options.add( new OptionsSetting( "main", testSpec.getMain() ) );

		List<Task> convert = this.createConvertTransformation( binarySpec, options, "convert", name );
		List<Task> compile = this.createCompileTransformation( binarySpec, options, "compile", name );

		// ------------------------------------------- //

		Task execute = this.createTestTask( binarySpec, testSpec );

		if( compile != null )
		{
			if( convert != null )
			{
				compile.get( compile.size() - 1 ).dependsOn( convert.get( convert.size() - 1 ) );
			}

			if( execute != null )
				execute.dependsOn( compile.get( compile.size() - 1 ) );
		}
		else if( convert != null )
		{
			if( execute != null )
				execute.dependsOn( convert.get( convert.size() - 1 ) );
		}

		// ------------------------------------------- //

		List<Task> result = new ArrayList<>();

		if( convert != null )
			result.addAll( convert );

		if( compile != null )
			result.addAll( compile );

		if( execute != null )
			result.add( execute );

		return result;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	//
	abstract protected List<Task> createConvertTransformation( IApplicationBinarySpec binarySpec, IOptions options,
	                                                           String prefix, String postfix );

	//
	abstract protected List<Task> createCompileTransformation( IApplicationBinarySpec binarySpec, IOptions options,
	                                                           String prefix, String postfix );

	//
	protected abstract Task createTestTask( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec );

	// ************************************************************************************* //
	// ************************************************************************************* //
	// HaxeTask:

	/**
	 */
	protected AHaxeTask createHaxeTask( IApplicationBinarySpec binarySpec, Class<? extends AHaxeTask> type,
	                                    IOptions options, String prefix, String postfix )
	{
		String name = NameUtil.getBinaryTaskName( binarySpec, prefix, postfix );
		File output = this.getOutputDirectory( binarySpec, prefix, postfix );

		//
		return TaskUtil.createTask( binarySpec, type, name, it ->
		{
			it.setTargetVariantCombination( binarySpec.getTargetVariantCombination() );
			it.setOptions( options );
			it.setOutputDir( output );
		} );
	}

}
