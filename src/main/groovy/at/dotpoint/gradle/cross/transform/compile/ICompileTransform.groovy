package at.dotpoint.gradle.cross.transform.compile

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.ITaskTransform
/**
 * Created by RK on 08.07.2016.
 */
interface ICompileTransform extends ITaskTransform< IApplicationBinarySpec, List<ISourceSet> >
{

}