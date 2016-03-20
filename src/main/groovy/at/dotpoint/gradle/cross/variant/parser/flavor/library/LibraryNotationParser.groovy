package at.dotpoint.gradle.cross.variant.parser.flavor.library

import at.dotpoint.gradle.cross.variant.parser.flavor.library.TypeConverter
import at.dotpoint.gradle.cross.variant.requirement.flavor.library.LibraryFlavorRequirement
import at.dotpoint.gradle.cross.variant.requirement.platform.PlatformRequirement
import org.gradle.internal.exceptions.DiagnosticsVisitor
import org.gradle.internal.typeconversion.*

/**
 * Created by RK on 19.02.16.
 */
public class LibraryNotationParser implements ILibraryNotationParser
{
    //
    private static final NotationParserBuilder<LibraryFlavorRequirement> BUILDER = NotationParserBuilder
            .toType( PlatformRequirement.class )
            .fromCharSequence( new StringConverter() )
            .converter( new TypeConverter() )

    //
    public static ILibraryNotationParser getInstance()
    {
        return new LibraryNotationParser( BUILDER.toComposite() );
    }

	// ------------------------------ //
	// ------------------------------ //

	private final NotationParser<Object,LibraryFlavorRequirement> parser;

	LibraryNotationParser( NotationParser<Object,LibraryFlavorRequirement> parser )
	{
		this.parser = parser;
	}

	@Override
	LibraryFlavorRequirement parseNotation(Object o) throws TypeConversionException {
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
class StringConverter implements NotationConverter<String, LibraryFlavorRequirement>
{
    @Override
    void convert( String notation, NotationConvertResult<? super LibraryFlavorRequirement> result ) throws TypeConversionException
    {
        result.converted( new LibraryFlavorRequirement( notation ) )
    }

    @Override
    void describe( DiagnosticsVisitor diagnosticsVisitor )
    {
        visitor.candidate("name of a library").example("TODO");
    }
}

/**
 *
 */
class TypeConverter extends TypedNotationConverter<String, LibraryFlavorRequirement>
{

	TypeConverter()
    {
        super( String.class )
    }

    @Override
    protected LibraryFlavorRequirement parseType( String notation )
    {
        return new LibraryFlavorRequirement( notation );
    }
}