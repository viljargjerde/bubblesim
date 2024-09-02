package inf101v22.grid;

import java.util.Objects;

public class Pair<T> {

	public T a;
	public T b;

	public Pair(T a, T b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Pair<T> other = (Pair<T>) obj;
		if (Objects.equals(this.a, other.a)) {
			return Objects.equals(this.b, other.b);
		} else if (Objects.equals(this.a, other.b)) {
			return Objects.equals(this.b, other.a);
		}
		return false;
	}

}
