package at.dotpoint.gradle.cross.variant.parser.flavor.executable

import at.dotpoint.gradle.cross.variant.parser.flavor.executable.TypeConverter
import at.dotpoint.gradle.cross.variant.requirement.flavor.executable.ExecutableFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.internal.exceptions.DiagnosticsVisitor
import org.gradle.internal.typeconversion.*

/**
 * Created by RK on 19.02.16.
 */
public class ExecutableNotationParser implements IExecutableNotationParser
{
    //
    private static final NotationParserBuilder<ExecutableFlavorRequirement> BUILDER = NotationParserBuilder
            .toType( PlatformRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new TypeConverter() )

    //
    public static IExecutableNotationParser getInstance()
    {
		return new ExecutableNotationParser( BUILDER.toComposite() );
	}

	// ------------------------------ //
	// ------------------------------ //

	private final NotationParser<Object,ExecutableFlavorRequirement> parser;

	ExecutableNotationParser( NotationParser<Object,ExecutableFlavorRequirement> parser )
	{
		this.parser = parser;
	}

	@Override
	ExecutableFlavorRequirement parseNotation(Object o) throws TypeConversionException {
		return this.parser.parseNotation(o);
	}

	@Override
	void describe(DiagnosticsVisitor diagnosticsVisitor) {
		this.parser.describe(diagnosticsVisitor);
	}
}

/**
 *
 */
class StringConverter implements NotationConverter<String, ExecutableFlavorRequirement>
{
    @Override
    void convert( String notation, NotationConvertResult<? super ExecutableFlavorRequirement> result ) throws TypeConversionException
    {
        result.converted( new ExecutableFlavorRequirement( notation ) )
    }

    @Override
    void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a executable").example("TODO");
    }
}

/**
 *
 */
class TypeConverter extends TypedNotationConverter<String, ExecutableFlavorRequirement>
{

	TypeConverter()
    {
        super( String.class )
    }

    @Override
    protected ExecutableFlavorRequirement parseType( String notation )
    {
        return new ExecutableFlavorRequirement( notation );
    }
}