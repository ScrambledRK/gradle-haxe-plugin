package at.dotpoint.gradle.cross.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.task.ICrossTask
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.JointCompileTaskConfig
/**
 * Created by RK on 02.07.2016.
 */
interface ICrossTransformTaskConfig extends JointCompileTaskConfig
{
	@Override
	Class<? extends ICrossTask> getTaskType();

	//@Override
	void configureTask( ICrossTask task, IApplicationBinarySpec binary, ISourceSet sourceSet, ServiceRegistry serviceRegistry );

	//@Override
	boolean canTransform( ISourceSet candidate);

	//@Override
	void configureAdditionalTransform( ICrossTask task, ISourceSet sourceSet );
}