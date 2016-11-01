package at.dotpoint.gradle.cross.dependency.builder;

import at.dotpoint.gradle.cross.dependency.model.IDependencySpec;
import org.gradle.platform.base.DependencySpecBuilder;

/**
 * Created by RK on 2016-08-21.
 */
public interface IDependencySpecBuilder extends DependencySpecBuilder
{
	@Override
	IDependencySpec build();
}