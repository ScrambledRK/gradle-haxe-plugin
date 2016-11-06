package at.dotpoint.gradle.cross.variant.target;

import org.gradle.api.Named;

import java.util.ArrayList;

/**
 *
 */
public class VariantCombination<TVariant extends Named> extends ArrayList<TVariant>
{


	/**
	 */
	public void setVariant( TVariant variant, Class<? extends TVariant> variantType )
	{
		int index = this.getIndexOfVariant( variantType );

		if( variant != null )
		{
			if( index != -1 ) 	this.set( index, variant );
			else				this.add( variant );
		}
		else
		{
			if( index != -1 )
				this.remove( index );
		}
	}

	/**
	 */
	public <TVariantType extends Named> TVariantType getVariant( Class<TVariantType> variantType )
	{
		int index = this.getIndexOfVariant( variantType );

		if( index != -1 )
			return variantType.cast( this.get( index ) );

		return null;
	}

	/**
	 */
	protected int getIndexOfVariant( Class<? extends Named> variantType )
	{
		for (int i = 0; i < this.size(); i++)
		{
			if( this.get(i).getClass().isAssignableFrom( variantType ) || variantType.isAssignableFrom( this.get(i).getClass() ) )
				return i;
		}

		return -1;
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 */
	public boolean isEqual( VariantCombination<TVariant> other )
	{
		if( this.size() != other.size() )
			return false;

		// ------------ //

		for( int i = 0; i < this.size(); i++ )
		{
			Class<? extends Named> variantType = this.get(i).getClass();

			if( !this.getVariant( variantType ).equals( other.getVariant( variantType ) ) )
				return false;
		}

		return true;
	}

	/**
	 */
	public boolean contains( VariantCombination<TVariant> other )
	{
		if( other.size() > this.size() )
			return false;

		// ------------ //

		for( int i = 0; i < other.size(); i++ )
		{
			Class<? extends Named> variantType = other.get(i).getClass();

			if( !this.getVariant( variantType ).equals( other.getVariant( variantType ) ) )
				return false;
		}

		return true;
	}

}
