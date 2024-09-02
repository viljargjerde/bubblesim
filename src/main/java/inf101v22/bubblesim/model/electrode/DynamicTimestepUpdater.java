/*
 * 
 */
package inf101v22.bubblesim.model.electrode;

import inf101v22.bubblesim.controller.Controller;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.vector.PosVector;

/**
 * The Class DynamicTimestepUpdater updates electrode by using a variable time
 * step. This is calculated by limiting the acceleration allowed in one
 * timestep.
 */
public class DynamicTimestepUpdater extends Updater {

	/**
	 * Instantiates a new dynamic timestep updater.
	 *
	 * @param electrode the electrode
	 */
	public DynamicTimestepUpdater(IElectrode electrode) {
		super(electrode);
	}

	/**
	 * Removes the sphereoids that are outside the frame.
	 */

	/**
	 * Iteratively moves a spheroid by applying bouyancy due to roation and gravity.
	 * Then calculates the maximum timestep by taking into account this force and
	 * drag-force. Repeats applying dragforce and surface-tension force untill the
	 * total time simulated reaches the time represented by one frame. Does not take
	 * into consideration any forces in the z-direction
	 */
	@Override
	void moveSpheroid(IFluidSpheroid fs) {
		double insideDensity = electrode.getProducedFluid().getDensity(electrode.getTemperature(),
				electrode.getPressure());
		double maxDeltaV = 0.0001;
		double outsideDensity = electrode.getMainFluid().getDensity(electrode.getTemperature(),
				electrode.getPressure());
		double deltaDensity = insideDensity - outsideDensity;
		double dt;
		double timeRemaining = 1.0 / Controller.fps;

		PosVector centAcc = electrode.getCentAcc(fs.getPosition().x);
		PosVector grav = electrode.getGravity();
		PosVector F_bouyancy = grav.add(centAcc).mult(fs.getVolume() * deltaDensity);
		do {
			// Bouyancy
			PosVector F_drag = electrode.getMainFluid().calculateDrag(fs, electrode.getFluidVelocity(fs.getPosition()),
					electrode.getTemperature(), electrode.getPressure());
			PosVector F_sum = PosVector.add(F_bouyancy, F_drag);

			// Calculate timestep dynamically to stop drag force from exploding
			dt = fs.getMass() / F_sum.mag() * maxDeltaV;
			if (dt > timeRemaining) {
				dt = timeRemaining;
			}
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
				dt = fs.getMass() / F_sum.mag() * maxDeltaV;
				if (dt > timeRemaining) {
					dt = timeRemaining;
				}
				fs.applyForce(F_sum);
				fs.update(dt);
				timeRemaining -= dt;
			}
		} while (timeRemaining > 0.0);

	}

}
