package at.dotpoint.gradle.cross.variant.target

import at.dotpoint.gradle.cross.variant.model.IVariant
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform

/**
 *
 * @param < TVariant >
 */
class VariantCombination<TVariant extends IVariant> extends ArrayList<TVariant>
{

	//
	public IPlatform getPlatform()
	{
		return this.getVariant( IPlatform.class );
	}

	public void setPlatform( IPlatform platform )
	{
		this.setVariant( platform, IPlatform.class );
	}

	//
	public IFlavor getFlavor()
	{
		return this.getVariant( IFlavor.class );
	}

	public void setFlavor( IFlavor flavor )
	{
		this.setVariant( flavor, IFlavor.class );
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 * @param variant
	 */
	public void setVariant( TVariant variant, Class<TVariant> variantType )
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
	 *
	 * @param variantType
	 * @return
	 */
	public <TVariantType extends TVariant> TVariantType getVariant( Class<TVariantType> variantType )
	{
		int index = this.getIndexOfVariant( variantType );

		if( index != -1 )
			return this.get( index );

		return null;
	}

	/**
	 *
	 * @return
	 */
	protected int getIndexOfVariant( Class<TVariant> variantType )
	{
		for (int i = 0; i < this.size(); i++)
		{
			if( this.get(i).class.isAssignableFrom( variantType ) || variantType.isAssignableFrom( this.get(i).class ) )
				return i;
		}

		return -1;
	}

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	/**
	 *
	 * @param other
	 * @return
	 */
	public boolean isEqual( VariantCombination<TVariant> other )
	{
		if( this.size() != other.size() )
			return false;

		// ------------ //

		for( int i = 0; i < this.size(); i++ )
		{
			Class<TVariant> variantType = this.get(i).class;

			if( this.getVariant( variantType ) != other.getVariant( variantType ) )
				return false;
		}

		return true;
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public boolean contains( VariantCombination<TVariant> other )
	{
		if( other.size() > this.size() )
			return false;

		// ------------ //

		for( int i = 0; i < other.size(); i++ )
		{
			Class<TVariant> variantType = other.get(i).class;

			if( this.getVariant( variantType ) != other.getVariant( variantType ) )
				return false;
		}

		return true;
	}
}
