package inf101v22.model.fluids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import inf101v22.bubblesim.model.fluids.Gas;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;

public class GasTest {

	@Test
	void testOxygen() {
		Gas O = Gas.OXYGEN;
		assertEquals(32.0 / 1000, O.getMolarMass());
		double densityAnswer = 1.283; // kg/m^3
		double calculated = O.getDensity(new Temperature(300), new Pressure(1, Units.Bar));
		assertTrue(calculated > 0);
		assertTrue(Math.abs(calculated - densityAnswer) < 0.001,
				"Expected " + densityAnswer + " but got " + calculated);
	}
}
