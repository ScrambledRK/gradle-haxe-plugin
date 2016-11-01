package at.dotpoint.gradle.cross.task;

import at.dotpoint.gradle.cross.variant.target.IVariationsSourceInternal;
import at.dotpoint.gradle.cross.variant.target.IVariationsTargetInternal;
import org.gradle.api.Task;

/**
 * Created by RK on 09.07.2016.
 */
public interface ICrossSourceTask extends Task, IVariationsSourceInternal, IVariationsTargetInternal
{

}