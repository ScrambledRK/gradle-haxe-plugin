package at.dotpoint.gradle.cross.task

import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import org.gradle.internal.service.ServiceRegistry

/**
 * dirty way to deal with this dirty LanguageTransform concept ...
 */
class TransformTask extends DefaultCrossTask
{
	public IApplicationBinarySpec binarySpec;
	public ServiceRegistry serviceRegistry;
}
