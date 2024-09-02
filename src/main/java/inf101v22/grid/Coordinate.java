/*
 * 
 */
package inf101v22.grid;

import java.util.Objects;

/**
 * Class to keep track of a coordinates in form of row, column.
 */
public class Coordinate {

	public final int row;

	public final int col;

	/**
	 * Instantiates a new coordinate.
	 *
	 * @param row the row
	 * @param col the column
	 */
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override

	public String toString() {
		return "{ row='" + row + "', col='" + col + "' }";
	}

	@Override
	public int hashCode() {
		return Objects.hash(col, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return col == other.col && row == other.row;
	}

}
