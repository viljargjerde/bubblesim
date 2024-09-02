/*
 * 
 */
package inf101v22.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A generic grid consisting of rows and columns of a generic object.
 *
 * @param <E> the element type
 */
// Inspirert av lab5 Grid
public class Grid<E> implements IGrid<E> {

	List<E> cells;

	private int rows;

	private int cols;

	/**
	 * Instantiates a new grid with default value null
	 *
	 * @param rows the rows of the grid
	 * @param cols the cols of the grid
	 */
	public Grid(int rows, int cols) {
		this(rows, cols, null);
	}

	/**
	 * Instantiates a new grid with given item as default value
	 *
	 * @param rows the number of rows in the grid
	 * @param cols the number of columns in the grid
	 * @param item the default item to fill the grid with
	 */
	public Grid(int rows, int cols, E item) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.cols = cols;
		cells = new ArrayList<E>(rows * cols);
		for (int i = 0; i < rows * cols; i++) {
			cells.add(item);
		}
	}

	@Override
	public Iterator<CoordinateItem<E>> iterator() {
		ArrayList<CoordinateItem<E>> list = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Coordinate pos = new Coordinate(i, j);
				list.add(new CoordinateItem<E>(pos, cells.get(getIndex(pos))));
			}
		}
		return list.iterator();
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public void set(Coordinate coordinate, E value) {
		if (!coordinateIsOnGrid(coordinate)) {
			throw new IndexOutOfBoundsException();
		}
		cells.set(getIndex(coordinate), value);

	}

	/**
	 * Helper-method to get the index in the 1D list that items are stored in for a
	 * given Coordinate.
	 *
	 * @param cord the coordinate to get the index for
	 * @return the index
	 */
	private int getIndex(Coordinate cord) {
		return cord.row + cord.col * rows;

	}

	@Override
	public E get(Coordinate coordinate) {
		if (!coordinateIsOnGrid(coordinate)) {
			throw new IndexOutOfBoundsException();
		}
		return cells.get(getIndex(coordinate));
	}

	@Override
	public boolean coordinateIsOnGrid(Coordinate coordinate) {
		return coordinate.row >= 0 && coordinate.row < rows && coordinate.col >= 0 && coordinate.col < cols;
	}

	@Override
	public List<E> getAsList() {
		return cells;
	}

}
