package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.model.compile.ACompileTransform
import at.dotpoint.gradle.cross.util.NameUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
/**
 * Created by RK on 27.02.16.
 */
class HaxeCompileTransform extends ACompileTransform
{

	//
	private LibraryBinaryResolver libraryBinaryResolver;

	//
	HaxeCompileTransform( LibraryBinaryResolver libraryBinaryResolver )
	{
		this.libraryBinaryResolver = libraryBinaryResolver
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 * SourceSet to convert
	 */
	@Override
	protected boolean isValidTransformTarget( IApplicationBinarySpec binarySpec )
	{
		return true
	}

	/**
	 * target VariantCombination to transform the target SourceSet to
     */
	@Override
	protected boolean isValidTransformInput( List<ISourceSet> sources )
	{
		return true;
	}

	/**
	 * actually creates all required tasks to convert a given SourceSet to the requested target variations. these
	 * tasks are not necessarily unique for each BinarySpec but are in combination of SourceSet and target variations.
	 *
	 * @param sourceSet SourceSet to cross convert to another SourceSet for a different Platform
	 * @param targetVariation IPlatform, IBuildType, IFlavor target specification for the transformation
	 * @param taskContainer container to create the tasks in, beware of naming
	 * @return top-level task to depend the conversion LifeCycle step on
	 */
	@Override
	Task createTransformTask( IApplicationBinarySpec binarySpec, List<ISourceSet> sources, TaskContainer taskContainer )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.targetVariantCombination;

		//
		Task generateTask = TaskUtil.createTaskContainerTask( taskContainer, GenerateHXMLTask.class,
				NameUtil.generateTransformTaskName( "generateHxml", binarySpec, targetVariation ) )
		{
			it.targetVariantCombination = binarySpec.targetVariantCombination.clone();
			it.sourceSets = sources;

			for( ISourceSet set : sources )
				this.getDependencies( set, targetVariation );
		};

		//
		Task executeTask = TaskUtil.createTaskContainerTask( taskContainer, ExecuteHXMLTask.class,
				NameUtil.generateTransformTaskName( "executeHxml", binarySpec, targetVariation ) )
		{
			it.hxmlFile = (generateTask as GenerateHXMLTask).hxmlFile;
		};

		executeTask.dependsOn generateTask;

		// -------- //

		return executeTask;
	}

	/**
	 *
	 * @param sourceSet
	 * @param targetVariation
	 */
	private void getDependencies( ISourceSet sourceSet, VariantCombination<IVariant> targetVariation )
	{
		for( IDependencySpec dependencySpec : sourceSet.dependencies.dependencies )
		{
			if( dependencySpec instanceof ILibraryDependencySpec )
			{
				ILibraryDependencySpec libraryDependencySpec = (ILibraryDependencySpec)dependencySpec;
				IApplicationBinarySpec applicationBinarySpec = this.libraryBinaryResolver.resolveBinary( libraryDependencySpec, targetVariation );

				println( "\n>> " + applicationBinarySpec );

				if( applicationBinarySpec != null )
				{
					applicationBinarySpec.sources.each {
						println( "src: " + it );
					}

					applicationBinarySpec.application.sources.each {
						println( "src: " + it );
					}
				}
			}
		}
	}
}