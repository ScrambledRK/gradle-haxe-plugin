package at.dotpoint.gradle.cross.variant.iterator;

import at.dotpoint.gradle.cross.variant.target.VariantCombination;
import org.gradle.api.Named;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RK on 20.03.16.
 */
public class VariantIterator<TVariant extends Named> implements Iterator<VariantCombination<TVariant>>
{
	//
	private final List<List<? extends TVariant>> collection;
	private final List<VariantCombination<TVariant>> permutations;

	//
	private int counter;

	/**
	 */
	public VariantIterator( List<List<? extends TVariant>> collection )
	{
		this.collection = collection;

		this.permutations = new ArrayList<>();
		this.counter = 0;
	}

	// ---------------------------------------- //
	// ---------------------------------------- //

	/**
	 */
	protected List<VariantCombination<TVariant>> getPermutations()
	{
		if( this.permutations.size() == 0 && this.collection.size() != 0 )
			this.generatePermutations();

		return this.permutations;
	}

	/**
	 */
	private void generatePermutations()
	{
		int numTotalPermutations = 1;

		for( List<? extends TVariant> aCollection1 : this.collection )
		{
			if( aCollection1.size() == 0 )
				continue;

			numTotalPermutations *= aCollection1.size();
		}

		// ----------------- //

		int numCurrentPermutations = 1;

		for( List<? extends TVariant> aCollection : this.collection )
		{
			if( aCollection.size() == 0 )
				continue;

			numCurrentPermutations *= aCollection.size();

			int repetitions = numTotalPermutations / numCurrentPermutations;
			int counter = 0;

			for( int j = 0; j < numCurrentPermutations; j++ )
			{
				for( int r = 0; r < repetitions; r++ )
				{
					if( this.permutations.size() <= counter )
						this.permutations.add( new VariantCombination<>() );

					this.permutations.get( counter ).add( aCollection.get( j % aCollection.size() ) );
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
	public boolean hasNext() {
		return this.counter < this.getPermutations().size();
	}

	/**
	 *
	 */
	@Override
	public VariantCombination<TVariant> next() {
		return this.getPermutations().get( this.counter++ );
	}

	/**
	 *
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}
}


