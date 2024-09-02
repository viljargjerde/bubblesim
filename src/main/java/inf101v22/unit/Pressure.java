package inf101v22.unit;

import java.util.HashMap;

public class Pressure implements IUnit {

	private double value;
	private Units defaultUnit = Units.Pa;
	private String description;

	private final static HashMap<Units, Double> conversionFactors = new HashMap<>();
	static {
		conversionFactors.put(Units.Pa, 1.0);
		conversionFactors.put(Units.Bar, 100000.0);
		conversionFactors.put(Units.atm, 101325.0);
	}

	public Pressure(double value, Units unit, String description) {
		this.value = Pressure.convertToPa(value, unit);
		this.description = description;
	}

	public Pressure(double value, Units unit) {
		this(value, unit, "");
	}

	public Pressure(double value) {
		this(value, Units.Pa, "");
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
		return Pressure.convertFromPa(value, unit);
	}

	@Override
	public void setValue(double newValue) {
		value = newValue;
	}

	@Override
	public void setValue(double newValue, Units unit) {
		value = Pressure.convertToPa(newValue, unit);
	}

	@Override
	public Units getUnit() {
		return defaultUnit;
	}

	static double convertFromPa(double value, Units unit) {

		return value / conversionFactors.get(unit);
	}

	static double convertToPa(double value, Units unit) {
		return value * conversionFactors.get(unit);
	}

}
