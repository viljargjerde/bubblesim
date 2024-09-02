/*
 * 
 */
package inf101v22.bubblesim.model.electrode;

/**
 * The Interface IElectrodeUpdater updates an electrode and all of its spheroids
 */
public interface IElectrodeUpdater {

	/**
	 * Start updater.
	 */
	void startUpdater();

	/**
	 * Gets whether or not the updater has completed the current update
	 *
	 * @return finished
	 */
	boolean getFinished();

	/**
	 * Used to tell the updater to start a new update by setting finished to false.
	 *
	 * @param finished the new finished
	 */
	/**
	 * @param finished
	 */
	void setFinished(boolean finished);

	/**
	 * Sets the number of spheroids produced per frame.
	 *
	 * @param newRate the new prouced per frame
	 */
	void setProucedPerFrame(int newRate);

	/**
	 * Gets the number of spheroids produced per frame.
	 *
	 * @return the number per frame
	 */
	int getProducedPerFrame();

	Double[] getCoveredFraction();
}
