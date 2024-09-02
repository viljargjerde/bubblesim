package inf101v22.model.electrode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import inf101v22.bubblesim.model.electrode.Electrode;
import inf101v22.bubblesim.model.fluids.FluidSpheroid;
import inf101v22.bubblesim.model.fluids.Gas;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.bubblesim.model.fluids.Water;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;
import inf101v22.vector.PosVector;

public class ElectrodeTest {

	@Test
	void testChangingPressureAndTemp() {
		Electrode el = new Electrode(0.1, new PosVector(0.1, 0.1, 0.1), new Pressure(2, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		IFluidSpheroid b1 = new FluidSpheroid(new PosVector(0.0, 0.0), el.getProducedFluid(), 0.000001, el);
		el.addSpheroid(b1);
		double V = b1.getVolume();
		el.setPressure(new Pressure(1, Units.Bar));
		assertEquals(V * 2, b1.getVolume());
		V = b1.getVolume();
		el.setTemperature(new Temperature(70, Units.C));
		assertTrue(V < b1.getVolume());
	}

	@Test
	void testGravity() {
		Electrode el = new Electrode(0.1, new PosVector(0.1, 0.1, 0.1), new Pressure(2, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		IFluidSpheroid b1 = new FluidSpheroid(new PosVector(0.0, 0.0), el.getProducedFluid(), 0.000001, el);
		el.addSpheroid(b1);
		assertEquals(-9.81, el.getGravity().y);
		// Should be zero but in case of some rounding error, some margin is allowed
		assertTrue(el.getGravity().x <= 1E-15);
	}
}
