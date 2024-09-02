/*
 * 
 */
package inf101v22.bubblesim.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import inf101v22.bubblesim.model.fluids.IFluidSpheroid;
import inf101v22.vector.PosVector;

public class ElectrodeView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ElectrodeViewable electrodeModel;
	double targetZoom = 1;
	double actualZoom = 1;
	private BufferedImage frame;
	private boolean newFrame = true;

	/**
	 * Instantiates a new electrode view.
	 *
	 * @param model the model to view
	 */
	public ElectrodeView(ElectrodeViewable model) {
		this.electrodeModel = model;
	}

	/**
	 * Does all the drawing of the "electrode frame", and returns a bufferedImage.
	 * Only does this when a timestep is completed and newFrame = true. If not,
	 * returns the same frame as last frame.
	 */
	private BufferedImage getImage() {
		if (frame == null || newFrame) { // Create new frame
			updateZoom();
			frame = new BufferedImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
			Graphics2D g2 = frame.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GRAY.brighter());
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2.translate(getWidth() / 2, getHeight() / 2);
			drawSpheroids(g2);
			g2.translate(-getWidth() / 2, -getHeight() / 2);
			drawScale(g2);
			displayGForce(g2);
			newFrame = false;
			g2.dispose();
		}
		return frame;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) (g);
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.drawImage(getImage(), 0, 0, this);

	}

	/**
	 * Updates the actual zoom level. Change in zoom is limited from frame to frame
	 * to not cause too sudden changes in zoom.
	 */
	private void updateZoom() {
		double diff = targetZoom - actualZoom;
		if (Math.abs(diff) <= 0.1) {
			actualZoom = targetZoom;
		} else if (diff < 0) {
			actualZoom -= 0.05;
		} else {
			actualZoom += 0.05;
		}
	}

	/**
	 * Display the calculated g-force in bottom left corner.
	 *
	 * @param g2
	 */
	private void displayGForce(Graphics2D g2) {
		double gForce = electrodeModel.getCentAcc(0).mag();
		gForce /= 9.81; // Convert from m/s**2 to g
		g2.drawString(String.format("%.2f", gForce) + " g", 30, getHeight() - 50);
	}

	/**
	 * Draws a scale to show the size of the spheroids compared to the real world.
	 * 
	 * @param g2
	 */
	private void drawScale(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));
		int width = this.getSize().width;
		int height = this.getSize().height;
		int lineWidth = width / 4;
		int xOffset = 20;
		int yOffset = 30;
		int verticalLinesHeight = 10;
		double electrodeWidth = electrodeModel.getSize().x;
		double scaleInMeter = (double) lineWidth / (double) this.getWidth() * electrodeWidth / actualZoom;

		// Horisontal scale line
		g2.drawLine(width - lineWidth - xOffset, height - yOffset, width - xOffset, height - yOffset);
//		Left vertical line
		g2.drawLine(width - lineWidth - xOffset, height - yOffset - verticalLinesHeight / 2,
				width - lineWidth - xOffset, height - yOffset + verticalLinesHeight / 2);
//		Right vertical line
		g2.drawLine(width - xOffset, height - yOffset - verticalLinesHeight / 2, width - xOffset,
				height - yOffset + verticalLinesHeight / 2);
		g2.setColor(Color.WHITE);
		Font font = new Font("SansSerif", Font.PLAIN, 24);
		g2.setFont(font);
		g2.drawString(String.format("%2.1e", scaleInMeter) + " m", width - lineWidth - xOffset + 40,
				height - yOffset - 10);

	}

	/**
	 * Draws all spheroids in their respective positions. Only draws spheroids that
	 * are visible. Also draws a small black circle representing the part of the
	 * spheroid in contact with the surface if this circle is big enough to be
	 * visible. Draws with y-axis pointing up to match expections
	 * 
	 * @param g2
	 */
	private void drawSpheroids(Graphics2D g2) {
		int r;
		int x;
		int y;
		double z;
		double electrodeWidth = electrodeModel.getSize().x;
		double electrodeHeight = electrodeModel.getSize().y;
		for (IFluidSpheroid fs : electrodeModel.getSpheroids()) {
			if (fs == null) {
				continue;
			}
			r = (convertXToPixel(fs.getRadius(), electrodeWidth));
			if (r > 0) {
				PosVector pos = fs.getPosition();
				x = convertXToPixel(pos.x, electrodeWidth) - (int) (getWidth() / 2 * actualZoom);
				y = convertYToPixel(pos.y, electrodeHeight) - (int) (getHeight() / 2 * actualZoom);
				// Turn coordinatesystem upside down to match convention in math and physics
				y = -y;
				z = pos.z;
				if (z < 0) {
					g2.setColor(Color.RED);
				} else {
					// Perimeter
					g2.setColor(Color.getHSBColor((float) (0.7), (float) (0.9), (float) (1 - z)));
					g2.drawOval(x - r, y - r, r * 2, r * 2);
					// Gradient fill
					Point2D center = new Point2D.Float(x, y);
					float[] dist = { 0.1f, 0.8f };
					Color[] colors = { new Color(66, 135, 245, 100), new Color(0, 0, 255, 100) };
					RadialGradientPaint p = new RadialGradientPaint(center, r, dist, colors);
					g2.setPaint(p);
					g2.fillOval(x - r, y - r, r * 2, r * 2);
				}
				g2.setColor(Color.BLACK);
				r = (convertXToPixel(fs.getContactRadius(), electrodeWidth));
				if (r > 0) {
					g2.drawOval(x - r, y - r, r * 2, r * 2);
				}
			}
		}
	}

	private int convertXToPixel(double x, double xSize) {
		return (int) (x / xSize * (this.getWidth()) * actualZoom);
	}

	private int convertYToPixel(double y, double ySize) {
		return (int) (y / ySize * this.getHeight() * actualZoom);
	}

	@Override
	public Dimension preferredSize() {
		int width = getParent().getWidth() / 2;
		int height = getParent().getWidth() / 2;
		return new Dimension(width, height);
	}

	public void setZoom(double newZoom) {
		targetZoom = newZoom;
	}

	public void setNewFrameState(boolean newState) {
		newFrame = newState;
	}

}
