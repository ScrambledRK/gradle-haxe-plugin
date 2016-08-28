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
		List<ISourceSet> sourceSets = this.getAllSourceSets( binarySpec, targetVariation );

		// ------------------------------------------- //

		//
		Task generateTask = TaskUtil.createTaskContainerTask( taskContainer, GenerateHXMLTask.class,
				binarySpec.tasks.taskName( "generateHxml" ) )
		{
			it.targetVariantCombination = binarySpec.targetVariantCombination.clone();
			it.sourceSets = sourceSets;
		};

		//
		Task executeTask = TaskUtil.createTaskContainerTask( taskContainer, ExecuteHXMLTask.class,
				binarySpec.tasks.taskName( "executeHxml" ) )
		{
			it.hxmlFile = (generateTask as GenerateHXMLTask).hxmlFile;
		};

		// ------------------------------------------- //

		for( ISourceSet set : sourceSets )
		{
			List<IApplicationBinarySpec> dependencies = this.getLibraryDependencies( set, targetVariation );

			dependencies.each { println( it ) }

			for( IApplicationBinarySpec dependency : dependencies )
				generateTask.dependsOn dependency.buildTask;
		}

		executeTask.dependsOn generateTask;

		return executeTask;
	}

	/**
	 *
	 * @param testBinary binary testing the sourceBinary
	 * @param input
	 * @param taskContainer
	 * @return
	 */
	@Override
	protected Task createTestTransformation( IApplicationBinarySpec testBinary,
                                             ILifeCycleTransformData input,
                                             TaskContainer taskContainer )
	{
		return null
	}

	// ---------------------------------------------------------------- //
	// ---------------------------------------------------------------- //

	/**
	 *
	 * @param binarySpec
	 * @return
	 */
	private List<ISourceSet> getAllSourceSets( IApplicationBinarySpec binarySpec,
	                                           VariantCombination<IVariant> targetVariation )
	{
		List<ISourceSet> directSourceSets = BinarySpecUtil.getSourceSetList( binarySpec );
		List<ISourceSet> allSourceSets = new ArrayList<ISourceSet>( directSourceSets );

		for( ISourceSet set : directSourceSets )
		{
			List<IApplicationBinarySpec> dependencies = this.getLibraryDependencies( set, targetVariation );

			for( IApplicationBinarySpec dependency : dependencies )
			{
				for( LanguageSourceSet depSet : dependency.sources.iterator() )
				{
					if( depSet instanceof ISourceSet )
						allSourceSets.add( depSet );
				}
			}
		}

		return allSourceSets;
	}

	/**
	 *
	 * @param sourceSet
	 * @param targetVariation
	 */
	private List<IApplicationBinarySpec> getLibraryDependencies( ISourceSet sourceSet,
	                                                             VariantCombination<IVariant> targetVariation )
	{
		List<IApplicationBinarySpec> dependencies = new ArrayList<>();

		for( IDependencySpec dependencySpec : sourceSet.dependencies.dependencies )
		{
			if( dependencySpec instanceof ILibraryDependencySpec )
			{
				ILibraryDependencySpec libraryDependencySpec = (ILibraryDependencySpec)dependencySpec;
				IApplicationBinarySpec applicationBinarySpec = this.libraryBinaryResolver.resolveBinary(
						libraryDependencySpec, targetVariation );

				if( applicationBinarySpec != null )
					dependencies.add( applicationBinarySpec );
			}
		}

		return dependencies;
	}
}