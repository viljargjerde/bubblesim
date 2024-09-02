package inf101v22.model.fluids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import inf101v22.bubblesim.model.electrode.Electrode;
import inf101v22.bubblesim.model.electrode.IElectrode;
import inf101v22.bubblesim.model.fluids.FluidSpheroid;
import inf101v22.bubblesim.model.fluids.Gas;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.bubblesim.model.fluids.Water;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;
import inf101v22.vector.PosVector;

public class TestFluidSpheroid {

	@Test
	void testGettingVolume() {
		IElectrode electrode = new Electrode(0.1, new PosVector(0.1, 0.1), new Pressure(1, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		FluidSpheroid sphere = new FluidSpheroid(new PosVector(0, 0.1), Gas.OXYGEN, 0.0001, electrode);

		double answer = 0.000083963; // m^3
		assertTrue(sphere.getMass() > 0);
		assertTrue(Gas.OXYGEN.getDensity(new Temperature(50, Units.C), new Pressure(1, Units.Bar)) > 0);
		assertTrue(Math.abs(answer - sphere.getVolume()) < 0.00001,
				"Got " + sphere.getVolume() + " but expected " + answer);
	}

	@Test
	void testGettingRadius() {
		IElectrode electrode = new Electrode(0.1, new PosVector(0.1, 0.1), new Pressure(1, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		FluidSpheroid sphere = new FluidSpheroid(new PosVector(0, 0.1), Gas.OXYGEN, 0.0001, electrode);
		double answer = 0.0271643789;
		double calculated = sphere.getRadius();
		assertTrue(calculated > 0);
		assertTrue(Math.abs(answer - calculated) < 0.00001, "Got " + calculated + " but expected " + answer);
	}

	@Test
	void testCombining() {
		IElectrode electrode = new Electrode(0.1, new PosVector(0.1, 0.1), new Pressure(1, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);

		// Radius = 0.0271643789
		FluidSpheroid a = new FluidSpheroid(new PosVector(0, 0.1), Gas.OXYGEN, 0.0001, electrode);
		FluidSpheroid b = new FluidSpheroid(new PosVector(0.035, 0.1), Gas.OXYGEN, 0.0001, electrode);
		assertTrue(a.collides(b));
		a.setVelocity(new PosVector(-1, 1, 1));
		b.setVelocity(new PosVector(1, 1, -1));
		a.combine(b);
		assertEquals(0.0002, a.getMass());
		assertEquals(new PosVector(0.0175, 0.1), a.getPosition());
		assertEquals(new PosVector(0, 1, 0), a.getVelocity());
	}

	@Test
	void testInsideFrame() {
		IElectrode electrode = new Electrode(0.1, new PosVector(0.1, 0.1), new Pressure(1, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);

		// Radius = 0.0271643789
		IFluidSpheroid a = new FluidSpheroid(new PosVector(0, 0.1), Gas.OXYGEN, 0.0001, electrode);
		assertTrue(a.insideFrame(new PosVector(0, 0), new PosVector(1, 1)));
		assertFalse(a.insideFrame(new PosVector(0, 0), new PosVector(1, 0.01)));
	}

}
