package at.dotpoint.gradle.cross.transform.model.compile;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec;
import at.dotpoint.gradle.cross.transform.model.ITaskTransform;

import java.util.List;

/**
 * Created by RK on 08.07.2016.
 */
public interface ICompileTransform
		extends ITaskTransform<IApplicationBinarySpec, List<ISourceSet>>
{

}