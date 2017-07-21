package at.dotpoint.gradle.haxe.transform.neko;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.util.NameUtil;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import at.dotpoint.gradle.haxe.task.AHaxeTask;
import at.dotpoint.gradle.haxe.task.neko.ExecuteNekoTask;
import at.dotpoint.gradle.haxe.task.neko.HaxeNekoTask;
import at.dotpoint.gradle.haxe.transform.AHaxeTransformation;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by RK on 27.02.16.
 */
public class NekoTransformation extends AHaxeTransformation
{
	//
	public NekoTransformation( ServiceRegistry serviceRegistry )
	{
		super( serviceRegistry );
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 */
	@Override
	protected boolean isValidTargetVariation( VariantCombination<IVariant> targetVariation )
	{
		return "neko".equals( targetVariation.getVariant( IPlatform.class ).getName() );
	}
	
	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 * Convert
	 */
	protected List<Task> createConvertTransformation( IApplicationBinarySpec binarySpec, IOptions options,
	                                                  String prefix, String postfix )
	{
		AHaxeTask task = this.createHaxeTask( binarySpec, HaxeNekoTask.class,
				options, prefix, postfix );

		return Collections.singletonList( task );
	}

	/**
	 * Compile
	 */
	protected List<Task> createCompileTransformation( IApplicationBinarySpec binarySpec, IOptions options,
	                                                  String prefix, String postfix )
	{
		return null;
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 */
	@Override
	protected Task createTestTask( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec )
	{
		String taskName = NameUtil.getBinaryTaskName( binarySpec, "test", testSpec.getName() );

		return TaskUtil.createTask( binarySpec, ExecuteNekoTask.class, taskName, it ->
		{
			it.setNekoFile( this.getBuildResultFile( binarySpec, testSpec, "convert", testSpec.getName() ) );
			//it.setMain( testSpec.getMain() );
		} );
	}

	//
	private File getBuildResultFile( IApplicationBinarySpec binarySpec, ITestComponentSpec testSpec,
	                                 String prefix, String postFix )
	{
		File libraryDir = this.getOutputDirectory( binarySpec, prefix, postFix );
		String fileName = testSpec.getMain().substring( testSpec.getMain().lastIndexOf( '.' ) + 1 ) + ".n";

		return new File( libraryDir, fileName );
	}
}