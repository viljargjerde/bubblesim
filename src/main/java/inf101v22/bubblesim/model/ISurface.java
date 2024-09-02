/*
 * 
 */
package inf101v22.bubblesim.model;

import inf101v22.vector.PosVector;

/**
 * The Interface ISurface describes a rectangle shaped surface in three
 * dimensions. Can also be used for a 2D rectangle
 */
public interface ISurface {

	/**
	 * Gets the upper left corner of the surface
	 *
	 * @return the position
	 */
	PosVector getPosition();

	/**
	 * Sets the upper left corner of the surface
	 *
	 * @param newPosition the new position
	 */
	void setPosition(PosVector newPosition);

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	PosVector getSize();

	/**
	 * Gets the angle that the surface is rotated.
	 *
	 * @return the angle
	 */
	double getAngle();

	/**
	 * Sets the rotation angle
	 *
	 * @param newAngle the new angle
	 */
	void setAngle(double newAngle);

}
