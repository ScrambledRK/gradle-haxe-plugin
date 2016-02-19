package at.dotpoint.gradle.platform

import org.gradle.internal.exceptions.DiagnosticsVisitor
import org.gradle.internal.typeconversion.MapNotationConverter
import org.gradle.internal.typeconversion.NotationConvertResult
import org.gradle.internal.typeconversion.NotationConverter
import org.gradle.internal.typeconversion.NotationParser
import org.gradle.internal.typeconversion.NotationParserBuilder
import org.gradle.internal.typeconversion.TypeConversionException
import org.gradle.internal.typeconversion.TypedNotationConverter
import org.gradle.platform.base.internal.DefaultPlatformRequirement
import org.gradle.platform.base.internal.PlatformRequirement

/**
 * Created by RK on 19.02.16.
 */
public class HaxePlatformNotationParser
{
    //
    private static final NotationParserBuilder<PlatformRequirement> BUILDER = NotationParserBuilder
            .toType( PlatformRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new HaxeConverter() )

    //
    public static NotationParser<Object,PlatformRequirement> getInstance()
    {
        return HaxePlatformNotationParser.BUILDER.toComposite();
    }
}

/**
 *
 */
class StringConverter implements NotationConverter<String, PlatformRequirement>
{
    @Override
    void convert( String notation, NotationConvertResult<? super PlatformRequirement> result ) throws TypeConversionException
    {
        result.converted( new DefaultPlatformRequirement( notation ) )
    }

    @Override
    void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a haxe-platform").example("TODO");
    }
}

/**
 *
 */
class HaxeConverter extends TypedNotationConverter<String, PlatformRequirement>
{

    HaxeConverter()
    {
        super( String.class )
    }

    @Override
    protected PlatformRequirement parseType( String notation )
    {
        return new DefaultPlatformRequirement( notation );
    }
}