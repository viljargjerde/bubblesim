package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import inf101v22.unit.Temperature;
import inf101v22.unit.Units;

public class TemperatureTest {

	@Test
	void testConversions() {

		Temperature K = new Temperature(300.15);
		Temperature C = new Temperature(27, Units.C);
		assertEquals(K.value(), C.value());
		assertEquals(Temperature.convertFromK(0, Units.C), -273.15);
		assertEquals(Temperature.convertToK(-273.15, Units.C), 0);
	}
}
