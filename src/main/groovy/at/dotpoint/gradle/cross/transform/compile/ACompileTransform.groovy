package at.dotpoint.gradle.cross.transform.compile

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.transform.ATaskTransform

/**
*  Created by RK on 02.07.2016.
*/
abstract class ACompileTransform extends ATaskTransform<IApplicationBinarySpec, List<ISourceSet>> implements ICompileTransform
{

}
