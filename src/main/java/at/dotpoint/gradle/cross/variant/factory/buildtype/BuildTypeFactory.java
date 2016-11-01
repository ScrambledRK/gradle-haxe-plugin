package at.dotpoint.gradle.cross.variant.factory.buildtype;

import at.dotpoint.gradle.cross.variant.factory.IVariantFactory;
import at.dotpoint.gradle.cross.variant.model.buildtype.BuildType;
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;

/**
 * Created by RK on 28.03.2016.
 */
public class BuildTypeFactory implements IVariantFactory<IBuildType>
{
	@Override
	public IBuildType create(String s) {
		return new BuildType( s );
	}
}
