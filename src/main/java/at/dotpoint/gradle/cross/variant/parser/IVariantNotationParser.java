package at.dotpoint.gradle.cross.variant.parser;

import at.dotpoint.gradle.cross.variant.requirement.IVariantRequirement;
import org.gradle.internal.typeconversion.NotationParser;

/**
 * Created by RK on 20.03.16.
 */
public interface IVariantNotationParser<TVariantRequirement extends IVariantRequirement>
		extends NotationParser<Object, TVariantRequirement> {

}