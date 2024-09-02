/*
 * 
 */
package inf101v22.bubblesim.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import inf101v22.bubblesim.Main;
import inf101v22.bubblesim.model.electrode.DynamicTimestepUpdater;
import inf101v22.bubblesim.model.electrode.Electrode;
import inf101v22.bubblesim.model.electrode.IElectrodeUpdater;
import inf101v22.bubblesim.model.electrode.StaticTimestepUpdater;
import inf101v22.bubblesim.model.fluids.Gas;
import inf101v22.bubblesim.model.fluids.Water;
import inf101v22.bubblesim.view.ElectrodeViewable;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;
import inf101v22.vector.PosVector;

/**
 * The Class Model stores all electrodes.
 */
public class Model {

	/** The views aka. electrodes */
	List<ElectrodeViewable> views;

	/** The updaters. */
	List<IElectrodeUpdater> updaters;

	Electrode anode;

	Electrode cathode;

	/**
	 * Instantiates a new model with two electrodes, one with hydrogen and one with
	 * oxygen spheroids. Both with water as main fluid.
	 */
	public Model() {
		this.views = new ArrayList<>();
		this.updaters = new ArrayList<>();
		this.anode = new Electrode(0.1, new PosVector(0.001, 0.001, 0.1), new Pressure(250, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		this.cathode = new Electrode(0.1, new PosVector(0.001, 0.001, 0.1), new Pressure(250, Units.Bar),
				new Temperature(50, Units.C), Gas.OXYGEN, Water.WATER);
		String updater = "static";
        Set<String> updater_options = new HashSet<>();
        updater_options.add("static");
        updater_options.add("dynamic");
        updater_options.add("rk4");
		for (String a : Main.args) {
			if (updater_options.contains(a.toLowerCase())) {
				updater = a.toLowerCase();
				break;
			}
		}
		switch (updater) {
		case "dynamic":
			this.updaters.add(new DynamicTimestepUpdater(anode));
			this.updaters.add(new DynamicTimestepUpdater(cathode));
			break;
		default: 
			this.updaters.add(new StaticTimestepUpdater(anode));
			this.updaters.add(new StaticTimestepUpdater(cathode));	
		}

		this.views.add(anode);
		this.views.add(cathode);
	}

	/**
	 * Changes a parameter like pressure in the anode, cathode or both depending on
	 * the code if the code contains 'a' the anode is changed, if the code contains
	 * 'c' the cathode is changed if neither 'a' nor 'c' is present both are
	 * changed. Here is a list of code letter and corresponding parameter
	 * 
	 * p : pressure, d : distance from center, t : temperature, r : RPM, v :
	 * water-velocity
	 * 
	 * The number following the code determines the new value. Some example of codes
	 * are as follows ap100 - This changes the anode pressure to 400 bar. r200 -
	 * This changes both the anode and cathode RPM to 200. The units correspond to
	 * the units in the UI, e.g. celsius and bar etc
	 *
	 * 
	 */
	public void setParameter(String parameterCode) {
		parameterCode = parameterCode.toLowerCase();
		double newValue = Double.parseDouble(parameterCode.replaceAll("[^0-9]", ""));
		List<Electrode> electrodesToChange = new ArrayList<>();
		if (parameterCode.contains("a")) {
			electrodesToChange.add(anode);
		} else if (parameterCode.contains("c")) {
			electrodesToChange.add(cathode);
		} else {
			electrodesToChange.add(anode);
			electrodesToChange.add(cathode);
		}

		if (parameterCode.contains("p")) {
			for (Electrode e : electrodesToChange) {
				e.setPressure(new Pressure(newValue, Units.Bar));
			}
		} else if (parameterCode.contains("d")) {
			for (Electrode e : electrodesToChange) {
				e.setDistanceFromCenter(newValue);
			}
		} else if (parameterCode.contains("t")) {
			for (Electrode e : electrodesToChange) {
				e.setTemperature(new Temperature(newValue, Units.C));
			}
		} else if (parameterCode.contains("r")) {
			for (Electrode e : electrodesToChange) {
				e.setRPM(newValue);
			}
		} else if (parameterCode.contains("v")) {
			for (Electrode e : electrodesToChange) {
				e.setFluidVelocity(new PosVector(newValue, 0));
			}
		}
	}

	/**
	 * Gets the electrode viewables.
	 *
	 * @return the electrode viewables
	 */
	public List<ElectrodeViewable> getElectrodeViewables() {
		return views;
	}

	public List<IElectrodeUpdater> getUpdaters() {
		return updaters;
	}

	/**
	 * Start updaters.
	 */
	public void startUpdaters() {

		for (IElectrodeUpdater updater : updaters) {
			updater.startUpdater();
		}
	}

	/**
	 * Checks if all updaters are finished
	 *
	 * @return true if all are finished, false if not
	 */
	public boolean updatersFinished() {
		for (IElectrodeUpdater updater : updaters) {
			if (!updater.getFinished()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if all electrodes are paused.
	 *
	 * @return true if all are paused
	 */
	public boolean allPaused() {
		for (ElectrodeViewable view : views) {
			if (!view.getPaused()) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Sets all updaters to finished = false, in order to start a new update
	 */
	public void resetUpdaters() {
		for (IElectrodeUpdater updater : updaters) {
			updater.setFinished(false);
		}
	}

	public void updateZoom() {

	}

}
