
package inf101v22.vector;

import java.util.Objects;

// TODO make class generic with the ability to give a dimension to the vector.
//Only allow adding vectors of same dimensionality such as position and velocity etc.
//This will reduce the potential for errors if physics simulation.
/**
 * The Class PosVector is a vector with 3 components, useful for positions
 * velocities, forces etc.
 */
public class PosVector {

	public double x;

	public double y;

	public double z;

	/**
	 * Instantiates a new posvector.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public PosVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Instantiates a new posvector.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public PosVector(double x, double y) {
		this(x, y, 0.0);
	}

	/**
	 * Adds the given vector to this vector
	 *
	 * @param other the other
	 * @return this after addition
	 */
	public PosVector add(PosVector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	/**
	 * Subtracts the given vector from this.
	 *
	 * @param other the other
	 * @return this after subtraction
	 */
	public PosVector sub(PosVector other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	/**
	 * Multiplies all the components of this vector with the given scalar
	 *
	 * @param scalar the scalar
	 * @return this after multiplication
	 */
	public PosVector mult(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	/**
	 * Divides all the components of this vector with the given scalar
	 *
	 * @param scalar the scalar
	 * @return this after division
	 */
	public PosVector div(double scalar) {
		this.x /= scalar;
		this.y /= scalar;
		this.z /= scalar;
		return this;
	}

	/**
	 * Calculates the distance from one vector to another
	 *
	 * @param other the other vector
	 * @return the distance
	 */
	public double dist(PosVector other) {
		return PosVector.sub(this, other).mag();
	}

	/**
	 * Calculates the distance squared from one vector to another
	 *
	 * @param other the other vector
	 * @return the distance squared
	 */
	public double distSq(PosVector other) {
		return PosVector.sub(this, other).magSq();
	}

	/**
	 * Calculates the magnitude or length of the vector
	 *
	 * @return the magnitude
	 */
	public double mag() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	/**
	 * Calculates the magnitude squared of the vector
	 *
	 * @return the magnitude squared
	 */
	public double magSq() {
		return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
	}

	/**
	 * Returns a new copy of vector
	 *
	 * @return copy
	 */
	public PosVector copy() {
		return new PosVector(x, y, z);
	}

	/**
	 * Normalizes vector -> scales all components so that length of vector is 1.
	 *
	 * @return the pos vector
	 */
	public PosVector normalize() {
		this.div(this.mag());
		return this;
	}

	/**
	 * Returns a new vector that is the sum of the given vectors
	 *
	 * @param vec1 the fist vector
	 * @param vec2 the second vector
	 * @return the result
	 */
	public static PosVector add(PosVector vec1, PosVector vec2) {
		return new PosVector(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
	}

	/**
	 * Returns a new vector that is the difference between the given vectors
	 *
	 * @param vec1 the fist vector
	 * @param vec2 the second vector
	 * @return the result
	 */
	public static PosVector sub(PosVector vec1, PosVector vec2) {
		return new PosVector(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
	}

	/**
	 * Returns a new vector that is the result of the given vector multiplied by the
	 * given scalar.
	 *
	 * @param vec1 the fist vector
	 * @param vec2 the second vector
	 * @return the result
	 */
	public static PosVector mult(PosVector posVector, double scalar) {
		return new PosVector(posVector.x * scalar, posVector.y * scalar, posVector.z * scalar);
	}

	/**
	 * Returns a new vector that is the result of the given vector divided by the
	 * given scalar
	 *
	 * @param vec1 the fist vector
	 * @param vec2 the second vector
	 * @return the result
	 */
	public static PosVector div(PosVector posVector, double scalar) {
		return new PosVector(posVector.x / scalar, posVector.y / scalar, posVector.z / scalar);
	}

	/**
	 * Returns a new vector with the same direction but with magnitude 1
	 *
	 * @param posVector the input vector
	 * @return normalized vector
	 */
	public static PosVector normalize(PosVector posVector) {
		double mag = posVector.mag();
		return new PosVector(posVector.x / mag, posVector.y / mag, posVector.z / mag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PosVector other = (PosVector) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y)
				&& Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
