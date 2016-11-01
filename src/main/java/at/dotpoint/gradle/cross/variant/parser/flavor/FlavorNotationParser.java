package at.dotpoint.gradle.cross.variant.parser.flavor;

import at.dotpoint.gradle.cross.variant.requirement.flavor.FlavorRequirement;
import org.gradle.internal.exceptions.DiagnosticsVisitor;
import org.gradle.internal.typeconversion.*;
/**
 * Created by RK on 19.02.16.
 */
public class FlavorNotationParser implements IFlavorNotationParser
{
    //
    private static final NotationParserBuilder<FlavorRequirement> BUILDER = NotationParserBuilder
            .toType( FlavorRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new TypeConverter() );

    //
    public static IFlavorNotationParser getInstance()
    {
        return new FlavorNotationParser( BUILDER.toComposite() );
    }

	// ------------------------------ //
	// ------------------------------ //

	private final NotationParser<Object,FlavorRequirement> parser;

	FlavorNotationParser( NotationParser<Object,FlavorRequirement> parser )
	{
		this.parser = parser;
	}

	@Override
	public FlavorRequirement parseNotation( Object o) throws TypeConversionException {
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
class StringConverter implements NotationConverter<String, FlavorRequirement>
{
    @Override
    public void convert( String notation, NotationConvertResult<? super FlavorRequirement> result ) throws TypeConversionException
    {
        result.converted( new FlavorRequirement( notation ) );
    }

    @Override
    public void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a flavor").example("TODO");
    }
}

/**
 *
 */
class TypeConverter extends TypedNotationConverter<String, FlavorRequirement>
{

	TypeConverter()
    {
        super( String.class )
    }

    @Override
    protected FlavorRequirement parseType( String notation )
    {
        return new FlavorRequirement( notation );
    }
}