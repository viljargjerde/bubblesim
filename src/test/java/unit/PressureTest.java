package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import inf101v22.unit.Pressure;
import inf101v22.unit.Units;

public class PressureTest {

	@Test
	void testConversions() {
		Pressure a = new Pressure(2, Units.Bar);
		assertEquals(200000, a.value());
		assertEquals(2, a.value(Units.Bar));
		a.setValue(2, Units.atm);
		assertEquals(202650, a.value());
		assertEquals(2, a.value(Units.atm));
	}

}
