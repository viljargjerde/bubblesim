package inf101v22.bubblesim.model.fluids;

import inf101v22.bubblesim.model.ISurface;
import inf101v22.vector.PosVector;

public interface IFluidSpheroid {

	PosVector getVelocity();

	PosVector getPosition();

	IFluid getFluid();

	double getRadius();

	double getVolume();

	double getMass();

	void updateConditions();

	void setVelocity(PosVector newVelocity);

	void setPosition(PosVector newPosition);

	boolean collides(IFluidSpheroid other);

	boolean collides(ISurface surface);

	boolean contains(PosVector position);

	void applyForce(PosVector force);

	void update(double stepsize);

	boolean insideFrame(PosVector pos, PosVector size);

	void combine(IFluidSpheroid other);

	double getContactRadius();

	IFluidSpheroid copy();

}