/*
 * 
 */
package inf101v22.bubblesim.view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import inf101v22.bubblesim.model.Model;

public class View extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Model model;
	List<ElectrodeWithUI> electrodes;
	JFrame frame;
	private boolean isHeadless;

	public View(Model model, boolean headless) {
		electrodes = new ArrayList<>();
		this.model = model;
		this.setLayout(new GridLayout());
		this.isHeadless = headless || GraphicsEnvironment.isHeadless();
		for (ElectrodeViewable view : model.getElectrodeViewables()) {
			ElectrodeWithUI e = new ElectrodeWithUI(view);
			electrodes.add(e);
			this.add(e);
		}
		if (!this.isHeadless) {
			this.frame = new JFrame("Bubble simulation inf101");
			this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.frame.setContentPane(this);
			this.frame.pack();
			this.frame.setVisible(true);
		}
	}

	/**
	 * Tells all electrodeviews to generate a new frame
	 */
	public void newFrame() {
		for (ElectrodeWithUI e : electrodes) {
			e.setNewFrameState(true);
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public boolean isHeadless() {
		return this.isHeadless;
	}

	@Override
	public Dimension preferredSize() {

		int width = 1500;
		int height = 750;
		return new Dimension(width, height);
	}
}
