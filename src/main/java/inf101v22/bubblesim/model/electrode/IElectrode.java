/*
 * 
 */
package inf101v22.bubblesim.model.electrode;

import java.util.List;

import inf101v22.bubblesim.model.ISurface;
import inf101v22.bubblesim.model.fluids.IFluid;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

// TODO: Auto-generated Javadoc
/**
 * The Interface IElectrode.
 */
public interface IElectrode extends ISurface {

	/**
	 * Sets the RPM in the model
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
	 * Gets the distance from center for the bottom of the cell.
	 *
	 * @return the distance from center
	 */
	double getDistanceFromCenter();

	/**
	 * Sets the distance from center to the bottom of the cell.
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
	 * Sets the temperature.
	 *
	 * @param newTemp the new temperature
	 */
	void setTemperature(Temperature newTemp);

	/**
	 * Gets centripetal acceleration in m/s**2 for a point relative to the bottom of
	 * the cell.
	 * 
	 * @param height relative to the bottom of the cell
	 * @return centripetal acceleration
	 */
	PosVector getCentAcc(double height);

	/**
	 * Gets the gravity in m/s**2
	 *
	 * @return the gravity
	 */
	PosVector getGravity();

	/**
	 * Gets a list of all fluid spheroids that currently exist in the electrode
	 * compartment
	 *
	 * @return the fluid spheroids
	 */
	List<IFluidSpheroid> getFluidSpheroids();

	/**
	 * Updates the electrode one timestep. Example of an update is rotate the
	 * electrode. This should not update any of the fluidspheroids as this is
	 * handled by an implementation of IElectrodeUpdater. The timestep is equal to
	 * 1/Controller.FPS
	 */
	void update();

	/**
	 * Gets the produced fluid, also known as the fluid that the spheroids are made
	 * of.
	 *
	 * @return the produced fluid
	 */
	IFluid getProducedFluid();

	/**
	 * Gets the main fluid.
	 *
	 * @return the main fluid
	 */
	IFluid getMainFluid();

	/**
	 * Gets the surface tension friction force.
	 *
	 * @param fs fluid spheroid
	 * @return the magnitude of surface tension friction force
	 */
	double getSurfaceTensionFrictionForce(IFluidSpheroid fs);

	/**
	 * Gets if paused
	 *
	 * @return true if paused
	 */
	boolean getPaused();

	/**
	 * Sets pause
	 *
	 * @param true to pause
	 */
	void setPaused(boolean state);

	/**
	 * Gets the fluid velocity relative to the electrode at a given position
	 *
	 * @param position the position
	 * @return the fluid velocity
	 */
	PosVector getFluidVelocity(PosVector position);

	/**
	 * Adds the spheroid to the electrode
	 *
	 * @param fs the fluidspheroid
	 */
	void addSpheroid(IFluidSpheroid fs);

}