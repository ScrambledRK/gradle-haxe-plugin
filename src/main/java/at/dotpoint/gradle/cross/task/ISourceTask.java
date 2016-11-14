package at.dotpoint.gradle.cross.task;

import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.variant.target.IVariationsSourceInternal;
import at.dotpoint.gradle.cross.variant.target.IVariationsTargetInternal;
import org.gradle.api.Task;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by RK on 09.07.2016.
 */
public interface ISourceTask extends Task, IVariationsSourceInternal, IVariationsTargetInternal
{
	/**
	 */
	List<ISourceSet> getSourceSets();

	/**
	 */
	void source( List<ISourceSet> sourceSets );
	void source( ISourceSet sourceSet );

	/**
	 */
	Set<File> getDependencies();

	/**
	 */
	void dependency( Set<File> dependencySet );
	void dependency( File dependency );
}