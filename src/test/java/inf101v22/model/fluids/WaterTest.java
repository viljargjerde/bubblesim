package inf101v22.model.fluids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import inf101v22.bubblesim.model.electrode.DynamicTimestepUpdater;
import inf101v22.bubblesim.model.electrode.Electrode;
import inf101v22.bubblesim.model.electrode.IElectrodeUpdater;
import inf101v22.bubblesim.model.fluids.FluidSpheroid;
import inf101v22.bubblesim.model.fluids.Gas;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.bubblesim.model.fluids.Water;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;
import inf101v22.vector.PosVector;

public class WaterTest {

	@Test
	void TestDensity() {
		double density = Water.WATER.getDensity(new Temperature(300), new Pressure(1, Units.Bar));
		double diff = 1000 - density;
		assertTrue(Math.abs(diff) < 3, "Density was " + density);
	}

	@Test
	void TestMolarMass() {
		assertEquals(18.0 / 1000, Water.WATER.getMolarMass());
	}

	@Test
	void testBuoyancy() {
		Electrode electrode = new Electrode(0.1, new PosVector(0.1, 0.1), new Pressure(50, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		IFluidSpheroid bubble = new FluidSpheroid(new PosVector(0, 0), Gas.OXYGEN, 0.00001, electrode);
		double insideDensity = Gas.OXYGEN.getDensity(electrode.getTemperature(), electrode.getPressure());
		double outsideDensity = Water.WATER.getDensity(electrode.getTemperature(), electrode.getPressure());
		double deltaDensity = insideDensity - outsideDensity;
		PosVector gravity = new PosVector(0, -9.81);
		PosVector F_bouyancy = gravity.mult(bubble.getVolume() * deltaDensity);
		bubble.applyForce(F_bouyancy);
		bubble.update(0.1);

		assertTrue(bubble.getVelocity().y < 100, "Too high velocity " + bubble.getVelocity().y);
	}

	@Test
	void TestDrag() {
		Electrode electrode = new Electrode(0.1, new PosVector(1, 1, 1), new Pressure(5, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		IElectrodeUpdater updater = new DynamicTimestepUpdater(electrode);
		electrode.setFluidVelocity(new PosVector(0, 0.01));
		IFluidSpheroid bubble = new FluidSpheroid(new PosVector(0.5, 0.5), Gas.OXYGEN, 0.000001, electrode);
		IFluidSpheroid bubble2 = new FluidSpheroid(new PosVector(0, 0), Gas.OXYGEN, 0.000001, electrode);
		bubble2.setVelocity(new PosVector(0.01, 0.01));
		electrode.addSpheroid(bubble);
		electrode.addSpheroid(bubble2);
		updater.setProucedPerFrame(0);
		updater.startUpdater();
		electrode.setPaused(false);
		// Run 10 iterations
		for (int i = 0; i < 10; i++) {
			while (!updater.getFinished()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			updater.setFinished(false);
		}
		// only check x to avoid gravity
		assertTrue(bubble.getPosition().x == 0.5, "x position should not move but moved to " + bubble.getPosition().x);
		assertTrue(bubble.getVelocity().x <= 1, "Maximum speed should be 1, but was " + bubble.getVelocity().y);
		assertTrue(bubble.getVelocity().x >= 0, "Velocity should be positive but was " + bubble.getVelocity().y);
		assertTrue(bubble.getVelocity().x <= bubble2.getVelocity().x,
				"Bubble 2 started with velocity and should have more than or equal velocity than the bubble that had none");
		assertTrue(bubble2.getVelocity().x <= 0.01, "Bubble should be braking but instead got more speed");
	}

}
