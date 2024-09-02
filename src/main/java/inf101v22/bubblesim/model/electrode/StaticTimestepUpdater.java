/*
 * 
 */
package inf101v22.bubblesim.model.electrode;

import inf101v22.bubblesim.controller.Controller;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.grid.Pair;
import inf101v22.vector.PosVector;

/**
 * @author viljar
 * 
 *         The Class StaticTimeStepUpdater updates electrode by using a constant
 *         time step that has to be very small to avoid instabilities
 *
 *
 */
public class StaticTimestepUpdater extends Updater {

	/**
	 * Instantiates a new dynamic timestep updater.
	 *
	 * @param electrode the electrode
	 */
	public StaticTimestepUpdater(IElectrode electrode) {
		super(electrode);
	}

	/**
	 * Iteratively moves a spheroid by applying bouyancy due to roation and gravity.
	 * Then calculates the maximum timestep by taking into account this force and
	 * drag-force. Repeats applying dragforce and surface-tension force untill the
	 * total time simulated reaches the time represented by one frame. Does not take
	 * into consideration any forces in the z-direction
	 *
	 * @param fs the fluidspheroid to move
	 * @return true, if moved
	 */
	@Override
	void moveSpheroid(IFluidSpheroid fs) {
		double dt = 1e-7;
		if (fs == null) {
			System.out.println("Spheroid is null");
		}
		if (Double.isNaN(fs.getPosition().x)) {
			System.out.println("NaN detected");
		}
		PosVector oldPos = fs.getPosition().copy();
		double insideDensity = electrode.getProducedFluid().getDensity(electrode.getTemperature(),
				electrode.getPressure());
		double outsideDensity = electrode.getMainFluid().getDensity(electrode.getTemperature(),
				electrode.getPressure());
		double deltaDensity = insideDensity - outsideDensity;
		double timeRemaining = 1.0 / Controller.fps / checksPerFrame;

		PosVector centAcc = electrode.getCentAcc(fs.getPosition().x);
		PosVector grav = electrode.getGravity();
		PosVector F_bouyancy = grav.add(centAcc);
		F_bouyancy.mult(fs.getVolume() * deltaDensity);
		do {
			// Bouyancy
			PosVector F_drag = electrode.getMainFluid().calculateDrag(fs, electrode.getFluidVelocity(fs.getPosition()),
					electrode.getTemperature(), electrode.getPressure());

			PosVector F_sum = PosVector.add(F_bouyancy, F_drag);
			// Breaking can't cause velocity to flip
			PosVector maxBreakingForce = fs.getVelocity().copy().mult(fs.getMass() / dt).add(PosVector.mult(F_sum, -1));
			// sxy = surfaceTension in xy - plane
			double F_sxyMag = electrode.getSurfaceTensionFrictionForce(fs);
			PosVector F_sxy;
			if (F_sxyMag > maxBreakingForce.mag()) {
				F_sxy = maxBreakingForce;
			} else {
				F_sxy = maxBreakingForce.normalize().mult(F_sxyMag);
			}
			F_sum.add(F_sxy);

			if (F_sum.mag() == 0) {
				timeRemaining = 0;
			} else {
				fs.applyForce(F_sum);
				fs.update(Math.min(dt, timeRemaining));
				timeRemaining -= Math.min(dt, timeRemaining);
			}
		} while (timeRemaining > 0.0);
		if (!oldPos.equals(fs.getPosition())) {
			moved.add(new Pair<PosVector>(oldPos, fs.getPosition()));
		}

	}

}
