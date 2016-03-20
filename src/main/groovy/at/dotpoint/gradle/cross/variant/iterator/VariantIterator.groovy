package at.dotpoint.gradle.cross.variant.iterator
/**
 * Created by RK on 20.03.16.
 */
class VariantIterator<TVariant> implements Iterator<VariantContainer<TVariant>>
{
	//
	private final List<List<TVariant>> collection;
	private final List<VariantContainer<TVariant>> permutations;

	//
	private int counter;

	/**
	 *
	 * @param collection
	 */
	VariantIterator( List<List<TVariant>> collection )
	{
		this.collection = collection;

		this.permutations = new ArrayList<VariantContainer<TVariant>>();
		this.counter = 0;
	}

	// ---------------------------------------- //
	// ---------------------------------------- //

	/**
	 *
	 * @return
	 */
	protected List<VariantContainer<TVariant>> getPermutations()
	{
		if( this.permutations.size() == 0 && this.collection.size() != 0 )
			this.generatePermutations();

		return this.permutations;
	}

	/**
	 *
	 */
	private void generatePermutations()
	{
		int numTotalPermutations = 1;

		for( int i = 0; i < this.collection.size(); i++ )
		{
			if( this.collection.get(i).size() == 0 )
				continue;

			numTotalPermutations *= this.collection.get(i).size();
		}

		// ----------------- //

		int numCurrentPermutations = 1;

		for( int k = 0; k < this.collection.size(); k++ )
		{
			if( this.collection.get(k).size() == 0 )
				continue;

			numCurrentPermutations *= this.collection.get(k).size();

			int repetitions = numTotalPermutations / numCurrentPermutations;
			int counter = 0;

			for( int j = 0; j < numCurrentPermutations; j++ )
			{
				for (int r = 0; r < repetitions; r++)
				{
					if( this.permutations.size() <= counter )
						this.permutations.add( new VariantContainer<TVariant>() );

					this.permutations.get(counter).add( this.collection.get(k).get( j % this.collection.get(k).size() ) );
					counter++;
				}
			}
		}
	}

	// ---------------------------------------- //
	// ---------------------------------------- //

	/**
	 *
	 */
	@Override
	boolean hasNext() {
		return this.counter < this.getPermutations().size();
	}

	/**
	 *
	 */
	@Override
	VariantContainer<TVariant> next() {
		return this.getPermutations().get( this.counter++ );
	}

	/**
	 *
	 */
	void remove() {
		throw new UnsupportedOperationException("remove");
	}
}

/**
 *
 * @param < TVariant >
 */
class VariantContainer<TVariant> extends ArrayList<TVariant>
{

	/**
	 *
	 * @param variantType
	 * @return
	 */
	public <TVariantType extends TVariant> TVariantType getVariant( Class<TVariantType> variantType )
	{
		for (int i = 0; i < this.size(); i++)
		{
			if ( this.get(i).class == variantType )
				return (TVariantType) this.get(i);
		}

		return null;
	}
}