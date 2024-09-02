/*
 * 
 */
package inf101v22.bubblesim.model.electrode;

import inf101v22.bubblesim.model.fluids.FluidSpheroid;
import inf101v22.bubblesim.model.fluids.IFluid;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.vector.PosVector;

/**
 * No longer in use. The Class ActiveSite is responsible for producing
 * fluidspheroids, and keeping track of whether or not it is covered.
 */
public class ActiveSite implements IActiveSite {

	private IFluid producedFluid;

	private PosVector pos;

	private boolean isCovered;

	private IElectrode electrode;

	private double newSpheroidMass = 0.000001;

	/**
	 * Instantiates a new active site.
	 *
	 * @param position  the position
	 * @param fluid     the fluid
	 * @param electrode the electrode
	 */
	public ActiveSite(PosVector position, IFluid fluid, IElectrode electrode) {
		this.pos = position;
		this.producedFluid = fluid;
		isCovered = false;
		this.electrode = electrode;
	}

	@Override
	public PosVector getPosition() {
		return pos.copy();
	}

	@Override
	public IFluid getProducedFluid() {
		return producedFluid;
	}

	@Override
	public boolean isCovered() {
		return isCovered;
	}

	@Override
	public void setCovered(boolean state) {
		isCovered = state;
	}

	@Override
	public IFluidSpheroid produce() {
		IFluidSpheroid fs = new FluidSpheroid(pos.copy(), producedFluid, newSpheroidMass, electrode);
		PosVector pos = fs.getPosition();
		pos.z = fs.getRadius() * 1.8;
		fs.setPosition(pos);
		return fs;
	}
}
