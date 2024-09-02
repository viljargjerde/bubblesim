package inf101v22.bubblesim.model.electrode;

import inf101v22.bubblesim.model.fluids.IFluid;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.vector.PosVector;

public interface IActiveSite {

	PosVector getPosition();

	IFluid getProducedFluid();

	boolean isCovered();

	void setCovered(boolean state);

	IFluidSpheroid produce();

}