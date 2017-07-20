package at.dotpoint.gradle.haxe.transform.javascript;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.specification.ITestComponentSpec;
import at.dotpoint.gradle.cross.util.TaskUtil;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import at.dotpoint.gradle.haxe.task.AHaxeTask;
import at.dotpoint.gradle.haxe.task.javascript.HaxeJavascriptTask;
import at.dotpoint.gradle.haxe.transform.AHaxeTransformation;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;

import java.util.Collections;
import java.util.List;

/**
 * Created by RK on 27.02.16.
 */
public class JavascriptTransformation extends AHaxeTransformation
{
	//
	public JavascriptTransformation( ServiceRegistry serviceRegistry )
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
		return "javascript".equals( targetVariation.getVariant( IPlatform.class ).getName() );
	}

	// ************************************************************************************* //
	// ************************************************************************************* //

	/**
	 * Convert
	 */
	protected List<Task> createConvertTransformation( IApplicationBinarySpec binarySpec, IOptions options,
	                                                  String prefix, String postfix )
	{
		AHaxeTask task = this.createHaxeTask( binarySpec, HaxeJavascriptTask.class,
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
		return  TaskUtil.createTask( binarySpec, DefaultTask.class, "test", null );
	}

}