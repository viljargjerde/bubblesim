/*
 * 
 */
package inf101v22.bubblesim.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import inf101v22.bubblesim.Main;
import inf101v22.bubblesim.model.Model;
import inf101v22.bubblesim.model.electrode.IElectrodeUpdater;
import inf101v22.bubblesim.view.View;

/**
 * The Class Controller starts timer and model updaters. Is also responsible for
 * taking screenshots of the screen after every timestep according to the
 * specified fps.
 */
public class Controller implements ActionListener {

	private View view;

	private Model model;

	private Timer timer;

	private int frameNum = 0;

	private int targetFrameNum;

	private static int slowMotionFactor = 200;

	/** Output video fps is 30 */
	public static int fps = 30 * slowMotionFactor;

	/** The path where frames are saved */
	private String path = "frames/";

	PrintWriter csvWriter;

	/**
	 * Instantiates a new controller.
	 *
	 * @param view  top level view
	 * @param model top level model
	 */
	public Controller(View view, Model model, int targetFrameNum) {
		this.view = view;
		this.model = model;
		this.targetFrameNum = targetFrameNum;
		timer = new Timer(1000 / fps, this);
		File folder = new File(path + String.join("_", Main.args) + "/");
		if (!folder.exists()) {
			folder.mkdir();
		}
		File outputFolder = new File("output/");
		if (!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		String filename;
		if (Main.args.length > 0) {
			filename = "output/" + String.join("_", Main.args) + ".csv";
		} else {
			filename = "output/percent_covered.csv";
		}
		File csvOutputFile = new File(filename);
		try {
			csvWriter = new PrintWriter(csvOutputFile);
			csvWriter.write(
					"anode covered from above;anode covered at surface;cathode covered from above;cathode covered at surface\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

// Saving image inspired by http://blog.gtiwari333.com/2012/04/java-capturesave-image-jframe-jpanel.html
	/**
	 * Takes a screenshot of a JComponent
	 *
	 * @param component the component to take screenshot of
	 * @return the screen shot
	 */
	public static BufferedImage getScreenShot(Component component) {

		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		component.paint(image.getGraphics());
		return image;
	}

	/**
	 * Gets a screenshot of the specified component and saves it as a png
	 *
	 * @param component the component
	 * @param fileName  the file name
	 * @return creates a png file
	 * @throws Exception if file could not be written
	 */
	public static void getSaveSnapShot(Component component, String fileName) throws Exception {
		BufferedImage img = getScreenShot(component);
		// write the captured image as a PNG
		ImageIO.write(img, "png", new File(fileName));
	}

	/**
	 * Starts timer and also starts the updaters running in seperate threads.
	 */
	public void startTimer() {
		timer.start();
		model.startUpdaters();
		if (Main.args.length > 1) {
			boolean anodePaused = true;
			boolean cathodePaused = true;
			String s;
			for (int i = 1; i < Main.args.length; i++) {
				s = Main.args[i];
				if (s.equals("headless"))
					continue;
				if (s.equals("dynamic"))
					continue;
				if (s.equals("static"))
					continue;
				if (s.charAt(0) == 'a') {
					anodePaused = false;
				} else if (s.charAt(0) == 'c') {
					cathodePaused = false;
				} else {
					cathodePaused = false;
					anodePaused = false;
				}
			}
			model.getElectrodeViewables().get(0).setPaused(anodePaused);
			model.getElectrodeViewables().get(1).setPaused(cathodePaused);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (frameNum >= targetFrameNum && targetFrameNum > 0) { // close window
			if (view.getFrame() != null && !view.isHeadless()) {
				view.getFrame().dispatchEvent(new WindowEvent(view.getFrame(), WindowEvent.WINDOW_CLOSING));
			} else {
				System.exit(0);
			}
		}

		if (e.getSource().equals(timer)) {
			if (!view.isHeadless()) {
				view.repaint();
			}
			if (model.updatersFinished() && !model.allPaused()) {
				view.newFrame();
				try {
					if (!view.isHeadless()) {
						getSaveSnapShot(view, path + frameNum + ".png");
					}
					System.out.print("Frame " + frameNum + ": ");
					 writeToCSV();
					frameNum++;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				model.resetUpdaters();
			}
		}
	}

	private void writeToCSV() {
		List<IElectrodeUpdater> updaters = model.getUpdaters();
		String outputLine = "";
		for (int i = 0; i < updaters.size(); i++) {
			Double[] fractions = updaters.get(i).getCoveredFraction();
			outputLine = outputLine.concat(fractions[0].toString() + ";");
			outputLine = outputLine.concat(fractions[1].toString() + ";");
		}
		outputLine = outputLine.concat("\n");
		csvWriter.append(outputLine);
		System.out.println("Writing: " + outputLine);
		csvWriter.flush();
	}

}
