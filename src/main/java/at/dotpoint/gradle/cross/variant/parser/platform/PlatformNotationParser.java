package at.dotpoint.gradle.cross.variant.parser.platform;

import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement;
import org.gradle.internal.exceptions.DiagnosticsVisitor;
import org.gradle.internal.typeconversion.*;


/**
 * Created by RK on 19.02.16.
 */
public class PlatformNotationParser implements IPlatformNotationParser
{
    //
    private static final NotationParserBuilder<PlatformRequirement> BUILDER = NotationParserBuilder
            .toType( PlatformRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new TypeConverter() );

    //
    public static IPlatformNotationParser getInstance()
    {
        return new PlatformNotationParser( BUILDER.toComposite() );
    }

	// ------------------------------ //
	// ------------------------------ //

	private final NotationParser<Object,PlatformRequirement> parser;

	PlatformNotationParser( NotationParser<Object,PlatformRequirement> parser )
	{
		this.parser = parser;
	}

	@Override
	public PlatformRequirement parseNotation(Object o) throws TypeConversionException {
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
class StringConverter implements NotationConverter<String, PlatformRequirement>
{
    @Override
    public void convert( String notation, NotationConvertResult<? super PlatformRequirement> result ) throws TypeConversionException
    {
        result.converted( new PlatformRequirement( notation ) );
    }

    @Override
    public void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a platform").example("TODO");
    }
}

/**
 *
 */
class TypeConverter extends TypedNotationConverter<String, PlatformRequirement>
{

	TypeConverter()
    {
        super( String.class );
    }

    @Override
    protected PlatformRequirement parseType( String notation )
    {
        return new PlatformRequirement( notation );
    }
}