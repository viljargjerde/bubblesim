/*
 * 
 */
package inf101v22.bubblesim.model.fluids;

import inf101v22.bubblesim.model.ISurface;
import inf101v22.bubblesim.model.electrode.IElectrode;
import inf101v22.vector.PosVector;

/**
 * The Class FluidSpheroid is one implementation of a IFluidSpheroids, where
 * among other things, the base-radius (radius in contact with surface) is
 * estimated and not measured, and it is assumed to be in contact with the
 * surface at all times.
 */
public class FluidSpheroid implements IFluidSpheroid {

	PosVector pos;

	PosVector vel;

	PosVector acc;

	private double mass; // Measured in kg

	private double volume; // m^3

	private double radius; // m

	final private IElectrode electrode;

	IFluid fluid;

	/**
	 * Instantiates a new fluid spheroid.
	 *
	 * @param pos       the pos
	 * @param fluid     the fluid
	 * @param mass      the mass
	 * @param electrode the electrode
	 */
	public FluidSpheroid(PosVector pos, IFluid fluid, double mass, IElectrode electrode) {
		this.mass = mass;
		this.fluid = fluid;
		this.electrode = electrode;
		this.pos = pos;
		this.vel = new PosVector(0.0, 0.0, 0.0);
		this.acc = new PosVector(0.0, 0.0, 0.0);
		updateConditions();
	}

	@Override
	public PosVector getVelocity() {
		return vel.copy();
	}

	@Override
	public void setVelocity(PosVector newVel) {
		this.vel = newVel;
	}

	@Override
	public void setPosition(PosVector newPos) {
		this.pos = newPos;
	}

	@Override
	public PosVector getPosition() {
		return pos.copy();
	}

	@Override
	public IFluid getFluid() {
		return fluid;
	}

	/**
	 * Calculate radius from mass
	 */
	private void calculateRadius() {
		radius = 0.6203504908994 * Math.cbrt(getVolume()); // r= cbrt(3/(4 pi) V)
	}

	/**
	 * Calculate volume from mass
	 */
	private void calculateVolume() {
		double density = fluid.getDensity(electrode.getTemperature(), electrode.getPressure());
		volume = mass / density;
	}

	@Override
	public void updateConditions() {
		calculateVolume();
		calculateRadius();
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public double getVolume() {
		return volume;
	}

	@Override
	public void applyForce(PosVector force) {
		PosVector forceCopy = force.copy();
		acc.add(forceCopy.div(mass));
	}

	@Override
	public void update(double dt) {
		pos.add(PosVector.mult(vel, dt));
		pos.add(PosVector.mult(acc, 0.5 * dt * dt));
		vel.add(acc.mult(dt));
		// Reset after each tick
		acc.mult(0);
	}

	@Override
	public boolean collides(IFluidSpheroid other) {

		return pos.distSq(other.getPosition()) < Math.pow(getRadius() + other.getRadius(), 2);
	}

	@Override
	public boolean collides(ISurface surface) {
		// TODO make it work regardless of angle of surface. Maybe by
		// rotating/transforming

		return pos.dist(surface.getPosition()) < surface.getSize().y + getRadius();
	}

	@Override
	public boolean contains(PosVector position) {
		return pos.distSq(position) < Math.pow(getRadius(), 2);
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public void combine(IFluidSpheroid other) {
		PosVector weightedPos = PosVector.add(pos.mult(getMass()), other.getPosition().mult(other.getMass()));
		PosVector momentum = PosVector.add(vel.mult(mass), other.getVelocity().mult(other.getMass()));
		double newMass = mass + other.getMass();

		pos = weightedPos.div(newMass);
		vel = momentum.div(newMass);
		mass = newMass;
		updateConditions();
		// the z-position that gives 0.025 base radius.
		pos.z = 0.999687451156 * getRadius();
	}

	@Override
	public boolean insideFrame(PosVector framePos, PosVector frameSize) {
		if (this.pos.x + this.getRadius() < framePos.x || this.pos.x - this.getRadius() > framePos.x + frameSize.x) {
			return false;
		}
		if (this.pos.y + this.getRadius() < framePos.y || this.pos.y - this.getRadius() > framePos.y + frameSize.y) {
			return false;
		}
		if (this.pos.z + this.getRadius() < framePos.z || this.pos.z - this.getRadius() > framePos.z + frameSize.z) {
			return false;
		}

		return true;
	}

	@Override
	public IFluidSpheroid copy() {

		return new FluidSpheroid(this.pos.copy(), this.fluid, this.mass, this.electrode);

	}

	@Override
	public double getContactRadius() {
		// eq.5
		// https://reader.elsevier.com/reader/sd/pii/S0017931018318404?token=58B7F35C82363ADC501393DB8FECF5503C53F65FF85B88950E465D3302905CC3602B567E4E618772A452E7E30795D461&originRegion=eu-west-1&originCreation=20220208094433
		return 0.025 * getRadius();
	}



}
