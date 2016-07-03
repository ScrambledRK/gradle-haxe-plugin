package at.dotpoint.gradle.cross.variant.iterator

/**
 *
 * @param < TVariant >
 */
class VariantCombination<TVariant> extends ArrayList<TVariant> {

	/**
	 *
	 * @param variantType
	 * @return
	 */
	public <TVariantType extends TVariant> TVariantType getVariant( Class<TVariantType> variantType )
	{
		for (int i = 0; i < this.size(); i++)
		{
			if( this.get(i).class.isAssignableFrom( variantType ) || variantType.isAssignableFrom( this.get(i).class ) )
				return (TVariantType) this.get(i);
		}

		return null;
	}

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
