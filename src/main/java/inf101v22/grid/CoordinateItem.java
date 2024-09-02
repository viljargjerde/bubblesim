/*
 * 
 */
package inf101v22.grid;

import java.util.Objects;

/**
 * A class to combine a Coordinate object and a generic item
 *
 * @param <E> the item type
 */
public class CoordinateItem<E> {

	public final Coordinate coordinate;

	public final E item;

	/**
	 * Instantiates a new coordinate item.
	 *
	 * @param cord the Coordinate of the item consisting of a row and a column
	 * @param item the item
	 */
	public CoordinateItem(Coordinate cord, E item) {
		coordinate = cord;
		this.item = item;
	}

	@Override

	public String toString() {

		return "{ coordinate='" + coordinate.toString() + "', item='" + item.toString() + "' }";
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordinate, item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		CoordinateItem other = (CoordinateItem) obj;
		return Objects.equals(coordinate, other.coordinate) && Objects.equals(item, other.item);
	}

}
