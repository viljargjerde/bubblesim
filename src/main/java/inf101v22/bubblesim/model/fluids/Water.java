package inf101v22.bubblesim.model.fluids;

import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

public class Water implements IFluid {
	double molarMass; // kg/mol

//	private static double maxDragChange = 0.00001; // m/s

	public Water() {
		this.molarMass = 18.0 / 1000;
	}

	@Override
	public double getViscosity(Temperature temp, Pressure pressure) {
		// Source:
		// https://www.fxsolver.com/browse/formulas/dynamic+viscosity+of+water+%28as+a+function+of+temperature+temperature%29#:~:text=The%20dynamic%20viscosity%20of%20water,to%20the%20normal%20boiling%20point.
		double A = 2.414E-5; // Pa*s
		double B = 247.8; // K
		double C = 140.0; // K
		return A * Math.pow(10, B / (temp.value() - C)); // Pa*s
	}

	@Override
	public double getDensity(Temperature temp, Pressure pressure) {
		// source: https://www.mdpi.com/1996-1073/11/12/3273/pdf-vor eq. 22
		// Valid for water only
		double A = 0.14395;
		double B = 0.0112;
		double C = 649.727;
		double D = 0.05107;
		return A / (Math.pow(B, (1 + Math.pow(1 - temp.value() / C, D)))); // kg/m^3
	}

	@Override
	public double getSurfaceTension() {
		// TODO temp dependent?

		// https://www.britannica.com/science/surface-tension

		return 0.07275; // J/m^2
	}

	@Override
	public PosVector calculateDrag(IFluidSpheroid spheroid, PosVector fluidVelocity, Temperature temp,
			Pressure pressure) {
		double density = getDensity(temp, pressure);
		PosVector relativeVelocity = PosVector.sub(fluidVelocity, spheroid.getVelocity());
		if (relativeVelocity.mag() < 1E-15) {
			return new PosVector(0, 0, 0);
		}
		double ReyNum = calculateReynoldsNum(spheroid.getRadius() * 2, temp, pressure, relativeVelocity.mag());
		double C_D = 24 / ReyNum * (1.0 + 0.15 * Math.pow(ReyNum, 0.687))
				+ 0.42 / (1.0 + 42500.0 * Math.pow(ReyNum, -1.16));
		double magnitude = 0.5 * C_D * density * relativeVelocity.magSq() * Math.PI * Math.pow(spheroid.getRadius(), 2);
		PosVector force = PosVector.normalize(relativeVelocity).mult(magnitude);
		return force;
	}

	private double calculateReynoldsNum(double L, Temperature temp, Pressure pressure, double relativeSpeed) {
		double density = getDensity(temp, pressure);
		double viscocity = getViscosity(temp, pressure);
		double rey = density * relativeSpeed * L / viscocity;
		if (rey <= 3E5) {
			return rey;
		}
		return 3E5;

	}

	@Override
	public double getMolarMass() {
		return molarMass;
	}

	public static Water WATER = new Water();

}
