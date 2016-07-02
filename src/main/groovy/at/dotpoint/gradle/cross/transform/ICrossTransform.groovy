package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.platform.base.TransformationFileType
/**
 * Created by RK on 02.07.2016.
 */
public interface ICrossTransform<TLanguageSourceSet extends ISourceSet, TTransformFileType extends TransformationFileType>
		extends LanguageTransform<TLanguageSourceSet,TTransformFileType>
{
	@Override
    ICrossTransformTaskConfig getTransformTask();

	//@Override
	boolean applyToBinary( IApplicationBinarySpec binary );
}