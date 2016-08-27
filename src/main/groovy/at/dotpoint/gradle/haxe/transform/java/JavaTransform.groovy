package at.dotpoint.gradle.haxe.transform.java

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec
import at.dotpoint.gradle.cross.dependency.model.ILibraryDependencySpec
import at.dotpoint.gradle.cross.dependency.resolver.LibraryBinaryResolver
import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransform
import at.dotpoint.gradle.cross.transform.model.lifecycle.ALifeCycleTransformData
import at.dotpoint.gradle.cross.transform.model.lifecycle.ILifeCycleTransformData
import at.dotpoint.gradle.cross.util.BinarySpecUtil
import at.dotpoint.gradle.cross.util.TaskUtil
import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.target.VariantCombination
import at.dotpoint.gradle.haxe.task.ExecuteHXMLTask
import at.dotpoint.gradle.haxe.task.GenerateHXMLTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.language.base.LanguageSourceSet
/**
 * Created by RK on 27.02.16.
 */
class JavaTransform extends ALifeCycleTransform
{

	//
	private LibraryBinaryResolver libraryBinaryResolver;

	//
	JavaTransform( LibraryBinaryResolver libraryBinaryResolver )
	{
		this.libraryBinaryResolver = libraryBinaryResolver
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @return
	 */
	@Override
	ILifeCycleTransformData createTransformData()
	{
		return new ALifeCycleTransformData();
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 * SourceSet to convert
	 */
	@Override
	protected boolean isValidTransformTarget( IApplicationBinarySpec binarySpec )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.targetVariantCombination;

		if( targetVariation.platform.name != "java" )
			return false;

		if( !this.isValidSourceSets( binarySpec.sources.iterator() ) )
			return false;

		if( !this.isValidSourceSets( binarySpec.application.sources.iterator() ) )
			return false;

		return true;
	}

	/**
	 *
	 * @param sourceSets
	 * @return
	 */
	private boolean isValidSourceSets( Iterator<LanguageSourceSet> sourceSets )
	{
		for( LanguageSourceSet sourceSet : sourceSets )
		{
			if( !(sourceSet instanceof ISourceSet) )
				return false;

			if( sourceSet.sourcePlatform.name != "haxe" )
				return false;
		}

		return true;
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //
	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	@Override
	protected Task createConvertTransformation( IApplicationBinarySpec binarySpec,
	                                            ILifeCycleTransformData input,
	                                            TaskContainer taskContainer )
	{
		return null
	}

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	@Override
	protected Task createCompileTransformation( IApplicationBinarySpec binarySpec,
	                                            ILifeCycleTransformData input,
	                                            TaskContainer taskContainer )
	{
		VariantCombination<IVariant> targetVariation = binarySpec.targetVariantCombination;
		List<ISourceSet> sourceSets = BinarySpecUtil.getSourceSetList( binarySpec );

		//
		Task generateTask = TaskUtil.createTaskContainerTask( taskContainer, GenerateHXMLTask.class,
				binarySpec.tasks.taskName( "generateHxml" ) )
		{
			it.targetVariantCombination = binarySpec.targetVariantCombination.clone();
			it.sourceSets = sourceSets;

			for( ISourceSet set : sourceSets )
				this.getDependencies( set, targetVariation );
		};

		//
		Task executeTask = TaskUtil.createTaskContainerTask( taskContainer, ExecuteHXMLTask.class,
				binarySpec.tasks.taskName( "executeHxml" ) )
		{
			it.hxmlFile = (generateTask as GenerateHXMLTask).hxmlFile;
		};

		executeTask.dependsOn generateTask;

		return executeTask;
	}

	/**
	 *
	 * @param binarySpec
	 * @param input
	 * @param taskContainer
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec binarySpec,
	                                         ILifeCycleTransformData input,
	                                         TaskContainer taskContainer )
	{
		return null
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

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