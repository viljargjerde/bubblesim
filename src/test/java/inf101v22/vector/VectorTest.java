package inf101v22.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VectorTest {

	@Test
	void testAdd() {
		PosVector vec1 = new PosVector(1, 2, 3);
		PosVector vec2 = new PosVector(4.01, 1, 1.99);
		PosVector added = PosVector.add(vec1, vec2);
		assertEquals(added.x, 5.01);
		assertEquals(added.y, 3);
		assertEquals(added.z, 4.99);
		vec1.add(vec2);
		assertEquals(vec1, added);
	}

	@Test
	void testSub() {
		PosVector vec1 = new PosVector(1, 2, 3);
		PosVector vec2 = new PosVector(4.01, 1, 1.99);
		PosVector subtracted = PosVector.sub(vec1, vec2);
		assertEquals(-3.01, subtracted.x);
		assertEquals(1, subtracted.y);
		assertEquals(1.01, subtracted.z);
		vec1.sub(vec2);
		assertEquals(vec1, subtracted);
	}

	@Test
	void testMagnitude() {
		PosVector vec = new PosVector(5, 2, -14);
		assertEquals(15, vec.mag());
	}

	@Test
	void testNormalize() {
		PosVector vec = new PosVector(5, 2, -14);
		PosVector norm = PosVector.normalize(vec);
		vec.normalize();
		assertEquals(1, vec.mag());
		assertEquals(1, norm.mag());
		vec.mult(1.22);
		assertEquals(1.22, vec.mag());
	}

	@Test
	void testDist() {
		PosVector vec1 = new PosVector(3, 5, 2);
		PosVector vec2 = new PosVector(0, 1, 2);
		assertEquals(5, vec1.dist(vec2));

	}
}
