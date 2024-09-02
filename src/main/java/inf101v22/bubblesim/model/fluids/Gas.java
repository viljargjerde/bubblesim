/*
 * 
 */
package inf101v22.bubblesim.model.fluids;

import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

/**
 * The Class Gas models an ideal gas, and contains static objects of hydrogen
 * and oxygen
 */
public class Gas implements IFluid {

	/** Gas constant in J/K/mol */
	static final double R = 8.3145; // J/k/mol

	/** The molar mass in kg/mol */
	private final double molarMass; // kg/mol

	/**
	 * Instantiates a new gas.
	 *
	 * @param molarMass the molar mass
	 */
	public Gas(double molarMass) {
		this.molarMass = molarMass;
	}

	@Override
	public double getViscosity(Temperature temp, Pressure pressure) {
		// TODO
		return 0;
	}

	@Override
	public double getDensity(Temperature temp, Pressure pressure) {
		return molarMass * pressure.value() / (R * temp.value());
	}

	@Override
	public double getMolarMass() {
		return molarMass;
	}

	public static Gas OXYGEN = new Gas(32.0 / 1000);

	public static Gas HYDROGEN = new Gas(2.0 / 1000);

	@Override
	public PosVector calculateDrag(IFluidSpheroid spheroid, PosVector fluidVelocity, Temperature temp, Pressure pressure) {
		// TODO
		return new PosVector(0, 0, 0);
	}

	@Override
	public double getSurfaceTension() {
		// TODO
		return 0;
	}

}
