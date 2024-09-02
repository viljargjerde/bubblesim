/*
 * 
 */
package inf101v22.unit;

/**
 * The Interface IUnit contains methods to save a value of a dimension and keep
 * track of unit conversions.
 */
public interface IUnit {

	/**
	 * Gets a description of what the parameter is used for, useful for for example
	 * tool tips etc.
	 *
	 * @return the description
	 */
	String getDescription();

	/**
	 * Sets the description.
	 *
	 * @param newDescription the new description
	 */
	void setDescription(String newDescription);

	/**
	 * Returns the stored value in SI units.
	 *
	 * @return value
	 */
	double value();

	/**
	 * Returns the stored value in the unit given
	 *
	 * @param unit the unit
	 * @return the converted value
	 */
	double value(Units unit);

	/**
	 * Updates stored value. Should only be given SI-units
	 *
	 * @param newValue the new value in SI-units
	 */
	void setValue(double newValue);

	/**
	 * Updates stored value. Converts from given unit to SI-units
	 *
	 * @param newValue the new value
	 * @param unit     the unit to convert from
	 */
	void setValue(double newValue, Units unit);

	/**
	 * Gets the default unit (SI)
	 *
	 * @return the unit
	 */
	Units getUnit();

}
