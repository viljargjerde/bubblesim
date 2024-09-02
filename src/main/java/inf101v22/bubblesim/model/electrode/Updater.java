package inf101v22.bubblesim.model.electrode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.danilopianini.util.FlexibleQuadTree;

import inf101v22.bubblesim.model.fluids.FluidSpheroid;
import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.grid.Pair;
import inf101v22.vector.PosVector;

public abstract class Updater implements IElectrodeUpdater {

	/** The electrode. */
	protected IElectrode electrode;
	/** The number of spheroids produced per frame. */
	private int spheroidsPerFrame = 10000;
	/** The standard new mass. */
	private double standardNewMass = 0.0000000000001;
	/** The generator. */
	private Random generator = new Random();
	/** The finished. */
	private boolean finished = false;
	/** The qt. */
	protected final FlexibleQuadTree<IFluidSpheroid> qt;
	protected Set<Pair<IFluidSpheroid>> collisions;
	protected Collection<Pair<PosVector>> moved;
	protected final int checksPerFrame = 10;

	public Updater(IElectrode electrode) {
		super();
		this.electrode = electrode;
		this.qt = new FlexibleQuadTree<>();
		Set<Pair<IFluidSpheroid>> collisions = new HashSet<Pair<IFluidSpheroid>>();
		this.collisions = Collections.synchronizedSet(collisions);
		this.moved = Collections.synchronizedCollection(new ArrayList<>());
	}
	
	


	@Override
	public Double[] getCoveredFraction() {
	    int gridResolution = 1000; // High resolution for better accuracy
	    double gridSizeX = electrode.getSize().x / gridResolution;
	    double gridSizeY = electrode.getSize().y / gridResolution;

	    // Grid to track coverage and surface coverage
	    boolean[][] coveredGrid = new boolean[gridResolution][gridResolution];
	    boolean[][] surfaceGrid = new boolean[gridResolution][gridResolution];


	    // Parallelize the process of iterating over spheroids
	    electrode.getFluidSpheroids().parallelStream().forEach(fs -> {
	        double radius = fs.getRadius();
	        double contactRadius = fs.getContactRadius();
	        PosVector pos = fs.getPosition();

	        // Convert sphere center to grid coordinates
	        int centerX = (int) (pos.x / gridSizeX);
	        int centerY = (int) (pos.y / gridSizeY);
	        int radiusInCellsX = (int) (radius / gridSizeX);
	        int radiusInCellsY = (int) (radius / gridSizeY);

	        // Iterate over grid cells within the sphere's bounding box
	        for (int x = Math.max(0, centerX - radiusInCellsX); x <= Math.min(gridResolution - 1, centerX + radiusInCellsX); x++) {
	            for (int y = Math.max(0, centerY - radiusInCellsY); y <= Math.min(gridResolution - 1, centerY + radiusInCellsY); y++) {
	                // Calculate the actual distance from the cell center to the sphere center
	                double gridCenterX = (x + 0.5) * gridSizeX;
	                double gridCenterY = (y + 0.5) * gridSizeY;
	                double distance_sq = Math.pow(gridCenterX - pos.x, 2) + Math.pow(gridCenterY - pos.y, 2);

	                // Check if the grid cell is within the sphere's radius
	                if (distance_sq <= radius*radius) {
	                        coveredGrid[x][y] = true;
	                    if (distance_sq <= contactRadius*contactRadius) {
	                            surfaceGrid[x][y] = true;
	                    }
	                }
	            }
	        }
	    });

	    // Calculate the fraction of covered cells
	    int coveredCells = 0;
	    int surfaceCells = 0;

	    for (int x = 0; x < gridResolution; x++) {
	        for (int y = 0; y < gridResolution; y++) {
	            if (coveredGrid[x][y]) {
	                coveredCells++;
	            }
	            if (surfaceGrid[x][y]) {
	                surfaceCells++;
	            }
	        }
	    }

	    double totalCells = gridResolution * gridResolution;
	    double coveredFraction = (double) coveredCells / totalCells;
	    double surfaceFraction = (double) surfaceCells / totalCells;

	    return new Double[]{coveredFraction, surfaceFraction};
	}




	/**
	 * Produce spheroids at random positions. Their size is normal distributed
	 * around "standardMass".
	 */
	private void produceSphereoids() {
		PosVector size = electrode.getSize();
		for (int i = 0; i < spheroidsPerFrame; i++) {
			PosVector pos = new PosVector(Math.random() * size.x, Math.random() * size.y);
			IFluidSpheroid fs = new FluidSpheroid(pos, electrode.getProducedFluid(), getNewMass(), electrode);
			pos.z = fs.getRadius() * 0.99;
			fs.setPosition(pos);
			// if (!checkNewSpheroid(fs)) { // Didnt collide with any spheroids.
			qt.insert(fs, fs.getPosition().x, fs.getPosition().y);
			electrode.addSpheroid(fs);
			// }
		}

	}

	/**
	 * Gets new normally distributed mass.
	 *
	 * @return the new mass
	 */
	private double getNewMass() {
		return Math.abs(generator.nextGaussian() * standardNewMass);
	}

	@Override
	public void startUpdater() {
		new Thread(this::update).start();
	}

	private void moveInTree(Pair<PosVector> posPair) {
		PosVector oldPos = posPair.a;
		PosVector newPos = posPair.b;
		IFluidSpheroid fs = qt.query(new double[] { oldPos.x - 1e-12, oldPos.y - 1e-12 },
				new double[] { oldPos.x + 1e-12, oldPos.y + 1e-12 }).get(0);
		qt.move(fs, new double[] { oldPos.x, oldPos.y }, new double[] { newPos.x, newPos.y });
	}

	abstract void moveSpheroid(IFluidSpheroid fs);

	/**
	 * Updates all spheroids in the electrode. After updating, waits until finished
	 * is set to false by controller to make sure all updates are recorded.
	 */
	private void update() {
		while (true) {

			if (!electrode.getPaused() && !finished) {
				List<IFluidSpheroid> spheroids = electrode.getFluidSpheroids();
				produceSphereoids();
				combineSphereoids();
				for (int i = 0; i < checksPerFrame; i++) {
					// Move spheroids using as many threads as are available
					this.moved.clear();
					spheroids.stream().parallel().forEach(this::moveSpheroid);
					for (Pair<PosVector> p : moved) {
						moveInTree(p);
					}
					combineSphereoids();
				}
				removeSphereoids();
				finished = true;
			
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Combine sphereoids.
	 */
	private void combineSphereoids() {
		collisions.clear();
		List<IFluidSpheroid> allSpheroids = electrode.getFluidSpheroids();
		allSpheroids.stream().parallel().forEach(this::checkSpheroid);
		HashSet<IFluidSpheroid> removed = new HashSet<>();
		
		for (Pair<IFluidSpheroid> p : collisions) {
			if (!removed.contains(p.a) && !removed.contains(p.b)) {
				PosVector prePos = p.a.getPosition();
				p.a.combine(p.b);
				qt.remove(p.b, p.b.getPosition().x, p.b.getPosition().y);
				qt.move(p.a, new double[] { prePos.x, prePos.y },
						new double[] { p.a.getPosition().x, p.a.getPosition().y });
				removed.add(p.b);

			}
		}
		allSpheroids.removeAll(removed);
	}

	private void removeSphereoids() {
	    List<IFluidSpheroid> fluidSpheroids = electrode.getFluidSpheroids();
	    PosVector size = electrode.getSize();
	    Iterator<IFluidSpheroid> iterator = fluidSpheroids.iterator();
	    
	    while (iterator.hasNext()) {
	        IFluidSpheroid fs = iterator.next();
	        if (!fs.insideFrame(new PosVector(0, 0), size) || fs.getPosition().z - fs.getRadius() > size.z) {
	            iterator.remove();  
	            qt.remove(fs, fs.getPosition().x, fs.getPosition().y);
	        }
	    }
	}


	/**
	 * Check spheroid.
	 *
	 * @param fs         the fs
	 * @param collisions the collisions
	 */
	private void checkSpheroid(IFluidSpheroid fs) {
		double x = fs.getPosition().x;
		double y = fs.getPosition().y;
		double r = fs.getRadius();
		List<IFluidSpheroid> nearbySpheroids = qt.query(x - 2 * r, y - 2 * r, x + 2 * r, y + 2 * r);
		for (IFluidSpheroid other : nearbySpheroids) {
			if (fs == other)
				continue;
			if (fs.collides(other)) {
				collisions.add(new Pair<IFluidSpheroid>(fs, other));
				break;
			}
		}
	}

	@Override
	public boolean getFinished() {
		return finished || electrode.getPaused();
	}

	@Override
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public void setProucedPerFrame(int newRate) {
		spheroidsPerFrame = newRate;
	}

	@Override
	public int getProducedPerFrame() {
		return spheroidsPerFrame;
	}

}