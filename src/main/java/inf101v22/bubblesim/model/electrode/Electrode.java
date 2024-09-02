/*
 */
package inf101v22.bubblesim.model.electrode;

import java.util.List;
import java.util.Vector;

import inf101v22.bubblesim.controller.Controller;
import inf101v22.bubblesim.model.fluids.IFluid;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.bubblesim.view.ElectrodeViewable;
import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.vector.PosVector;

/**
 * The Class Electrode represents an electrode with pressure temperature and a
 * surface that is capable of rotating. Also includes all the spheroids in the
 * electrode.
 *
 */
public class Electrode implements IElectrode, ElectrodeViewable {
	// Jaroslaw Drelich; Jan D. Miller (1994). The Effect of Solid Surface
	// Heterogeneity and Roughness on the Contact Angle/Drop (Bubble) Size
	// Relationship. , 164(1), 252–259. doi:10.1006/jcis.1994.1164
	private static final double avrgContactAngle = 0.92;
	// Diff source:
	// https://pdf.sciencedirectassets.com/271533/1-s2.0-S0169433214X00061/1-s2.0-S0169433214000889/main.pdf?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEDQaCXVzLWVhc3QtMSJIMEYCIQC%2FOdiUwL8r86M0xXcWVDNzVy%2FHxQttaxiENs0c7nq2iQIhAI3qnFk%2FO3z7778nA9zktP%2F%2Bg3moMkrsaE4BQ7mwGMQPKoMECN3%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEQBBoMMDU5MDAzNTQ2ODY1Igz1ZoxCiRLBs1hKncMq1wMFZzJlZUYFl%2Fm5H0tRyfhVVZTN5c2zjLz3k9pLbZWjg8A7utNREHx30%2BeK9z%2FQJMxMTM%2Fq3mW03MHI%2BzhNNATlloM4ich24%2BZewHz7puC%2B6ka0ryjl0ruslNcuiIwm6FXcO%2FmUhysrkVPg2DA6J%2FVcb5M%2BXB8Jgzrx1d%2Fv0p87MLE5sinz%2B2S2hfus71CSDGbGIEdXW%2Bd%2B8wvukAmgv%2F5zNTROkDC2GV6q2x%2F2UH%2BdbTs6Phj%2Fea2oJ2RcC3J6XOnentMHgeuyrzOjftr0gbgfktaDwYm3AfDMQ6hB2YjBb2BaPXEK5UGzg0oCTEtYRKSYRwFZ5sGUiTzaxCQXyfW8qizjuTXaxlf7Zp8DflsRJ6KDUp7bXS1cmyBbmxtHLlwVb6n%2BAidFpT2Wbvby367TSpWFZdFrKTSdiIpZPrthA1HQgGZC5peuULOerW8XS%2F7wRY8pZiQURfNT09q%2BPNkywHHbajstlaDkQP4nUboAZqu9n%2Fapqe33oKLJ8U2JePTccplqbxmn1OtV3vlAfKev7g8RX6DpsNOS9JnAEVBqr6Ph1iTbt4doCw84RwQ1KzkNn0eB4iTEGSlPDkbASvA3iXvP4mzTFoFsW%2BOjK337xMQNRsckrRYwlPX2kgY6pAFUX8cg4qtFOp8rFMyECmST39T6Spje3FGKacignwH4utcbRyiRIfm8J%2B%2BvVoHiS44a8Q3cgQvHiT4uCN2SOT%2BviQtRaFG0KeFJckUfvxEsgjnlOLDDKZQDRw5JK%2Fih1RR01ts4cQLRAqWAoZwU2LCYaw9qrbdoSyu2SeDw4gLd1XmYQYy%2Bj14NPe6XS82hhnTkVi%2Biw5MfpHY2aSbeSNVeZDYifQ%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220418T201318Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIAQ3PHCVTY3LTLON7X%2F20220418%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=d300b0dc701022a7d34ee1f99efdbcd621a0a28843c1bd821937840d44288253&hash=9c5c9822b3220aafb9c8b44db74dd02621946f30bd413acc1b4d8d4de347e7c5&host=68042c943591013ac2b2430a89b270f6af2c76d8dfd086a07176afe7c76c2c61&pii=S0169433214000889&tid=spdf-06dedd52-9df0-46b7-b410-9fcca530f4bc&sid=70107d637e6c874a42593e14df82eb869bb6gxrqb&type=client
	private static final double alpha = Math.acos(avrgContactAngle) - 0.2338741;
	private static final double beta = Math.acos(avrgContactAngle) + 0.2338741;
	private static final double surfaceTensionFrctionCoeff = 2 * Math.PI * (alpha - beta)
			/ (Math.PI * Math.PI - Math.pow(alpha - beta, 2)) * (Math.sin(alpha) + Math.sin(beta));
	PosVector pos; // Position relative to the center in m

	/** The size of the electrode: width, height, depth in m */
	final PosVector size; //

	double rotationAngle = 0.0;

	Pressure pressure;

	Temperature temp;

	PosVector fluidVel;

	private double RPM;

	List<IFluidSpheroid> fluidSpheroids;

	/** The fluid that new spheroids are made from */
	final IFluid producedFluid;

	final IFluid mainFluid;

	private boolean paused = true;

	/**
	 * Instantiates a new electrode.
	 *
	 * @param distanceFromCenter the distance from center in m
	 * @param size               the size: width, height, depth in m
	 * @param pressure           the pressure
	 * @param temp               the temperature
	 * @param producedFluid      the produced fluid
	 * @param mainFluid          the main fluid
	 */
	public Electrode(double distanceFromCenter, PosVector size, Pressure pressure, Temperature temp,
			IFluid producedFluid, IFluid mainFluid) {
		pos = new PosVector(distanceFromCenter, 0);
		this.size = size;
		this.pressure = pressure;
		this.temp = temp;
		this.RPM = 0;
		fluidSpheroids = new Vector<>();
		this.producedFluid = producedFluid;
		this.mainFluid = mainFluid;
		this.fluidVel = new PosVector(0, 0, 0);

	}

	@Override
	public void setRPM(double newRPM) {
		RPM = newRPM;
	}

	@Override
	public double getRPM() {
		return RPM;
	}

	@Override
	public double getDistanceFromCenter() {
		return pos.x;
	}

	@Override
	public void setDistanceFromCenter(double newDist) {
		pos.x = newDist;
	}

	@Override
	public void setPressure(Pressure newPressure) {
		pressure.setValue(newPressure.value());
		updateConditions();
	}

	private void updateConditions() {
		for (IFluidSpheroid fs : fluidSpheroids) {
			fs.updateConditions();
		}

	}

	@Override
	public Pressure getPressure() {
		return pressure;
	}

	@Override
	public void setTemperature(Temperature newTemp) {
		temp.setValue(newTemp.value());
		updateConditions();
	}

	@Override
	public Temperature getTemperature() {
		return temp;
	}

	@Override
	public PosVector getCentAcc(double height) {
		double r = height + this.pos.x;
		return new PosVector(Math.pow(Math.PI * RPM / 30, 2) * r, 0);

	}

	@Override
	public double getSurfaceTensionFrictionForce(IFluidSpheroid fs) {
		// https://reader.elsevier.com/reader/sd/pii/S0017931018318404?token=4DD196026D2C1CB90F29DA11768BB1F8C83327BFBCAC406BE0636BF5959616B773C2AFEDB60998726D0C9C827C0914AC&originRegion=eu-west-1&originCreation=20220619214925
		double magnitude = fs.getContactRadius() * surfaceTensionFrctionCoeff * mainFluid.getSurfaceTension();
		return Math.abs(magnitude);
	}

	@Override
	public PosVector getGravity() {
		double angle = -this.rotationAngle - Math.PI / 2;
		return new PosVector(9.81 * Math.cos(angle), 9.81 * Math.sin(angle));
	}

	@Override
	public void update() {

		rotationAngle += 2 * Math.PI * RPM / 60 / Controller.fps;

	}

	@Override
	public PosVector getPosition() {
		return pos;
	}

	@Override
	public void setPosition(PosVector newPosition) {
		pos = newPosition;
	}

	@Override
	public PosVector getSize() {
		return size;
	}

	@Override
	public double getAngle() {
		return rotationAngle;
	}

	@Override
	public void setAngle(double newAngle) {
		this.rotationAngle = newAngle;
	}

	@Override
	public IFluidSpheroid[] getSpheroids() {
		IFluidSpheroid[] arr = new IFluidSpheroid[fluidSpheroids.size()];
		return fluidSpheroids.toArray(arr);
	}

	@Override
	public boolean getPaused() {
		return paused;
	}

	@Override
	public void setPaused(boolean state) {
		this.paused = state;
	}

	@Override
	public List<IFluidSpheroid> getFluidSpheroids() {
		return fluidSpheroids;
	}

	@Override
	public IFluid getProducedFluid() {
		return producedFluid;
	}

	@Override
	public IFluid getMainFluid() {
		return mainFluid;
	}

	@Override
	public void setFluidVelocity(PosVector newVel) {
		fluidVel = newVel;
	}

	@Override
	public PosVector getFluidVelocity(PosVector position) {
		// Right now the same regardless of position, but might be necessary to know
		// position in another implementation
		return fluidVel.copy();
	}

	@Override
	public PosVector getFluidVelocity() {
		return fluidVel;
	}

	@Override
	public void addSpheroid(IFluidSpheroid fs) {
		fluidSpheroids.add(fs);
	}

}
