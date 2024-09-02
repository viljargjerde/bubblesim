/*
 * 
 */
package inf101v22.bubblesim.model.fluids;

import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

/**
 * The Interface IFluid describes fluids, both liquids and gases
 */
public interface IFluid {

	/**
	 * Gets the viscosity.
	 *
	 * @param temp     the temp
	 * @param pressure the pressure
	 * @return the viscosity in Pa*s
	 */
	double getViscosity(Temperature temp, Pressure pressure);

	/**
	 * Gets the density.
	 *
	 * @param temp     the temp
	 * @param pressure the pressure
	 * @return the density in kg/m^3
	 */
	double getDensity(Temperature temp, Pressure pressure);

	/**
	 * Gets the molar mass.
	 *
	 * @return the molar mass in kg/mol
	 */
	double getMolarMass();

	/**
	 * Calculates the drag-force on a spheroid.
	 *
	 * @param spheroid      the spheroid
	 * @param fluidVelocity the fluid velocity relative to the surface, not the
	 *                      spheroid
	 * @param temp          the temp
	 * @param pressure      the pressure
	 * @return drag-force
	 */
	PosVector calculateDrag(IFluidSpheroid spheroid, PosVector fluidVelocity, Temperature temp, Pressure pressure);

	/**
	 * Gets the surface tension.
	 *
	 * @return the surface tension in J/m^2
	 */
	double getSurfaceTension();

}
