package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.TransformationFileType
/**
 * Created by RK on 02.07.2016.
 */
abstract class ACrossTransform<TLanguageSourceSet extends ISourceSet, TTransformFileType extends TransformationFileType>
		implements ICrossTransform<TLanguageSourceSet,TTransformFileType>
{

	/**
	 * The language name.
	 */
	@Override
	String getLanguageName()
	{
		return "CrossPlatform";
	}

	/**
	 * The interface type of the language source set.
	 */
	@Override
	Class<TLanguageSourceSet> getSourceSetType()
	{
		return ISourceSet.class;
	}

	/**
	 * The output type generated from these language sources.
	 */
	@Override
	Class<TTransformFileType> getOutputType()
	{
		return ISourceSet.class;
	}

	/**
	 * The tool extensions that should be added to any binary with these language sources.
	 */
	@Override
	Map<String, Class<?>> getBinaryTools()
	{
		return Collections.emptyMap();
	}

	// ----------------------------- //
	// ----------------------------- //

	@Override
	abstract ICrossTransformTaskConfig getTransformTask();

	@Override
	abstract boolean applyToBinary(IApplicationBinarySpec binary);

	@Override
	boolean applyToBinary(BinarySpec binary)
	{
		if( binary instanceof IApplicationBinarySpec )
			return this.applyToBinary( (IApplicationBinarySpec) binary );

		return false;
	}
}
