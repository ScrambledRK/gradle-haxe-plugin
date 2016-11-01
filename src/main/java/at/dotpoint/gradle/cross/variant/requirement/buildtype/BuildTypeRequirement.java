package at.dotpoint.gradle.cross.variant.requirement.buildtype;

import at.dotpoint.gradle.cross.util.DefaultNamed;
import at.dotpoint.gradle.cross.variant.model.IVariant;
import at.dotpoint.gradle.cross.variant.model.buildtype.IBuildType;
/**
 * Created by RK on 11.03.16.
 */
public class BuildTypeRequirement extends DefaultNamed implements IBuildTypeRequirement
{

	public BuildTypeRequirement( String name) {
		super(name);
	}

	@Override
	public Class<? extends IVariant> getVariantType() {
		return IBuildType.class;
	}
}
