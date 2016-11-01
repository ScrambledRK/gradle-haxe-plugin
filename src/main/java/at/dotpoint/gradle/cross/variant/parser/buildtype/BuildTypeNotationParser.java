package at.dotpoint.gradle.cross.variant.parser.buildtype;

import at.dotpoint.gradle.cross.variant.requirement.buildtype.BuildTypeRequirement;
import org.gradle.internal.exceptions.DiagnosticsVisitor;
import org.gradle.internal.typeconversion.*;
/**
 * Created by RK on 19.02.16.
 */
public class BuildTypeNotationParser implements IBuildTypeNotationParser
{
    //
    private static final NotationParserBuilder<BuildTypeRequirement> BUILDER = NotationParserBuilder
            .toType( BuildTypeRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new TypeConverter() );

    //
    public static IBuildTypeNotationParser getInstance()
    {
        return new BuildTypeNotationParser( BUILDER.toComposite() );
    }

	// ------------------------------ //
	// ------------------------------ //

	private final NotationParser<Object,BuildTypeRequirement> parser;

	BuildTypeNotationParser( NotationParser<Object,BuildTypeRequirement> parser )
	{
		this.parser = parser;
	}

	@Override
	BuildTypeRequirement parseNotation( Object o ) throws TypeConversionException {
		return this.parser.parseNotation(o);
	}

	@Override
	public void describe(DiagnosticsVisitor diagnosticsVisitor) {
		this.parser.describe(diagnosticsVisitor);
	}
}

/**
 *
 */
class StringConverter implements NotationConverter<String, BuildTypeRequirement>
{
    @Override
    public void convert( String notation, NotationConvertResult<? super BuildTypeRequirement> result ) throws TypeConversionException
    {
        result.converted( new BuildTypeRequirement( notation ) );
    }

    @Override
    public void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a buildType").example("TODO");
    }
}

/**
 *
 */
class TypeConverter extends TypedNotationConverter<String, BuildTypeRequirement>
{

	TypeConverter()
    {
        super( String.class );
    }

    @Override
    protected BuildTypeRequirement parseType( String notation )
    {
        return new BuildTypeRequirement( notation );
    }
}