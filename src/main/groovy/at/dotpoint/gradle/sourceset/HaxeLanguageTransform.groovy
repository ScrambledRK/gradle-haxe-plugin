package at.dotpoint.gradle.sourceset

import at.dotpoint.gradle.model.HaxeApplicationBinarySpec
import at.dotpoint.gradle.platform.HaxePlatform
import at.dotpoint.gradle.tasks.ConvertSourceTask
import org.gradle.api.DefaultTask
import org.gradle.internal.service.ServiceRegistry
import org.gradle.jvm.internal.SourceSetDependencyResolvingClasspath
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.java.tasks.PlatformJavaCompile
import org.gradle.model.internal.manage.schema.ModelSchemaStore
import org.gradle.platform.base.BinarySpec
import org.gradle.api.Task

/**
 * Created by RK on 27.02.16.
 */
class HaxeLanguageTransform implements LanguageTransform<HaxeSourceSet, HaxeSourceSet>
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
       public Class<HaxeSourceSet> getSourceSetType() {
           return HaxeSourceSet.class;
       }

       @Override
       public Map<String, Class<?>> getBinaryTools() {
           return Collections.emptyMap();
       }

       @Override
       public Class<HaxeSourceSet> getOutputType() {
           return HaxeSourceSet.class;
       }

       @Override
       public SourceTransformTaskConfig getTransformTask() {
           return new HaxeSourceTransformTaskConfig();
       }

       @Override
       public boolean applyToBinary(BinarySpec binary) {
           return binary instanceof HaxeApplicationBinarySpec;
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
		   return ConvertSourceTask.class;
		}

		@Override
		public void configureTask( Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry )
		{
		   ConvertSourceTask convertSourceTask = (ConvertSourceTask) task;
		   HaxeSourceSet haxeSourceSet = (HaxeSourceSet) sourceSet;
		   HaxeApplicationBinarySpec haxeApplicationBinarySpec = (HaxeApplicationBinarySpec) binary;

		   haxeApplicationBinarySpec.builtBy(convertSourceTask);

		   convertSourceTask.setDescription(String.format("Converts %s.", haxeSourceSet));
		   convertSourceTask.setSource(haxeSourceSet.getSource());
		   convertSourceTask.dependsOn(haxeSourceSet);

		   HaxePlatform targetPlatform = haxeApplicationBinarySpec.getTargetPlatform();

		   convertSourceTask.setOutputPlatform(targetPlatform);

			println("configureTask: " + convertSourceTask + " @: " + haxeApplicationBinarySpec );
		}

	}
}