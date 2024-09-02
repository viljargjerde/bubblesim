package inf101v22.unit;

// Kunne kanskje brukt en abstract class for å slippe å skrive en del av metodene for hver enhet
public class Temperature implements IUnit {
	private double value;
	private String description;
	private Units defaultUnit = Units.K;

	public Temperature(double value, Units unit, String description) {
		this.value = Temperature.convertToK(value, unit);
		this.description = description;
	}

	public Temperature(double value, Units unit) {
		this(value, unit, "");
	}

	public Temperature(double value) {
		this(value, Units.K, "");
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String newDescription) {
		description = newDescription;
	}

	@Override
	public double value() {
		return value;
	}

	@Override
	public double value(Units unit) {
		return Temperature.convertFromK(value, unit);
	}

	@Override
	public void setValue(double newValue) {
		value = newValue;
	}

	@Override
	public void setValue(double newValue, Units unit) {
		value = Temperature.convertToK(newValue, unit);
	}

	@Override
	public Units getUnit() {
		return defaultUnit;
	}

	public static double convertToK(double value, Units unit) {
		if (unit.equals(Units.K)) {
			return value;
		} else {
			return value + 273.15;
		}
	}

	public static double convertFromK(double value, Units unit) {
		if (unit.equals(Units.K)) {
			return value;
		} else {
			return value - 273.15;
		}
	}
}
