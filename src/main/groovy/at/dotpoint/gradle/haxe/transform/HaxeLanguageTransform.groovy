package at.dotpoint.gradle.haxe.transform

import at.dotpoint.gradle.cross.sourceset.ISourceSet
import at.dotpoint.gradle.cross.specification.IApplicationBinarySpec
import at.dotpoint.gradle.cross.task.AConvertSourceTask
import at.dotpoint.gradle.cross.variant.model.platform.Platform
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.platform.base.BinarySpec

/**
 * Created by RK on 27.02.16.
 */
class HaxeLanguageTransform implements LanguageTransform<ISourceSet, ISourceSet>
{
       public HaxeLanguageTransform()
	   {

       }

		// -------------------------------------- //
		// -------------------------------------- //

       @Override
       public String getLanguageName() {
           return "haxe";
       }

       @Override
       public Class<ISourceSet> getSourceSetType() {
           return ISourceSet.class;
       }

       @Override
       public Map<String, Class<?>> getBinaryTools() {
           return Collections.emptyMap();
       }

       @Override
       public Class<ISourceSet> getOutputType() {
           return ISourceSet.class;
       }

       @Override
       public SourceTransformTaskConfig getTransformTask() {
           return new HaxeSourceTransformTaskConfig();
       }

       @Override
       public boolean applyToBinary(BinarySpec binary) {
           return binary instanceof IApplicationBinarySpec;
       }

		// -------------------------------------- //
		// -------------------------------------- //

	/**
	*
	*/
	public static class HaxeSourceTransformTaskConfig implements SourceTransformTaskConfig {

		private HaxeSourceTransformTaskConfig()
		{

		}

		@Override
		public String getTaskPrefix() {
		   return "convert";
		}

		@Override
		public Class<? extends DefaultTask> getTaskType() {
		   return AConvertSourceTask.class;
		}

		@Override
		public void configureTask( Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry )
		{
			AConvertSourceTask convertSourceTask = (AConvertSourceTask) task;
			ISourceSet iSourceSet = (ISourceSet) sourceSet;
			IApplicationBinarySpec haxeApplicationBinarySpec = (IApplicationBinarySpec) binary;

			haxeApplicationBinarySpec.builtBy(convertSourceTask);

			convertSourceTask.setDescription(String.format("Converts %s.", iSourceSet));
			convertSourceTask.setSource(iSourceSet.getSource());
			convertSourceTask.dependsOn(iSourceSet);

			Platform targetPlatform = haxeApplicationBinarySpec.getTargetPlatform();
			Platform originPlatform = iSourceSet.getSourcePlatform();

			convertSourceTask.setOutputPlatform(targetPlatform);
			convertSourceTask.setInputPlatform(originPlatform);
		}

	}
}