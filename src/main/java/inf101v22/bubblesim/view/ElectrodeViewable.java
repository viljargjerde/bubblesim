/*
 * 
 */
package inf101v22.bubblesim.view;

import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

// TODO: Auto-generated Javadoc
/**
 * The Interface ElectrodeViewable.
 */
public interface ElectrodeViewable {

	/**
	 * Gets all spheroids in electrode.
	 *
	 * @return the spheroids
	 */
	IFluidSpheroid[] getSpheroids();

	/**
	 * Gets the size of the electrode in m.
	 *
	 * @return the size
	 */
	PosVector getSize();

	/**
	 * Gets whether or not the electrode is paused.
	 *
	 * @return true if paused
	 */
	boolean getPaused();

	/**
	 * Pauses or unpauses electrode.
	 *
	 * @param state the new paused
	 */
	void setPaused(boolean state);

	/**
	 * Sets the RPM.
	 *
	 * @param newRPM the new RPM
	 */
	void setRPM(double newRPM);

	/**
	 * Gets the RPM.
	 *
	 * @return the RPM
	 */
	double getRPM();

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	Temperature getTemperature();

	/**
	 * Sets the temperature.
	 *
	 * @param newTemp the new temperature
	 */
	void setTemperature(Temperature newTemp);

	/**
	 * Gets the distance from center in m.
	 *
	 * @return the distance from center
	 */
	double getDistanceFromCenter();

	/**
	 * Sets the distance from center in m.
	 *
	 * @param newDist the new distance from center
	 */
	void setDistanceFromCenter(double newDist);

	/**
	 * Sets the pressure.
	 *
	 * @param newPressure the new pressure
	 */
	void setPressure(Pressure newPressure);

	/**
	 * Gets the pressure.
	 *
	 * @return the pressure
	 */
	Pressure getPressure();

	/**
	 * Gets centripetal acceleration in m/s**2 for a point relative to the bottom of
	 * the cell.
	 * 
	 * @param height relative to the bottom of the cell
	 * @return centripetal acceleration
	 */
	public PosVector getCentAcc(double height);

	/**
	 * Gets the velocity of the fluid inside the electrode relative to the
	 * electrode.
	 *
	 * @return the fluid velocity
	 */
	PosVector getFluidVelocity();

	/**
	 * Sets the velocity of the fluid inside the electrode relative to the
	 * electrode.
	 *
	 * @param newVel the new fluid velocity
	 */
	void setFluidVelocity(PosVector newVel);

}
