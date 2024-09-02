package inf101v22.bubblesim.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author viljar
 *
 */
public class ElectrodeWithUI extends JPanel implements ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ElectrodeView view;
	JSlider zoomSlider;

	public ElectrodeWithUI(ElectrodeViewable electrodeModel) {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		view = new ElectrodeView(electrodeModel);
		zoomSlider = new JSlider(JSlider.VERTICAL, 100, 1000, 100);
		zoomSlider.addChangeListener(this);
		zoomSlider.setSnapToTicks(false);
		zoomSlider.setMinorTickSpacing(1);
		this.add(zoomSlider, "West");
		this.add(view, "Center");
		this.add(new ParameterChanger(electrodeModel), "South");
	}

	/**
	 * Tells the electrodeview to that a new frame is ready to be rendered (if
	 * newState = true)
	 * 
	 * @param newState true to start new frame
	 */
	public void setNewFrameState(boolean newState) {
		view.setNewFrameState(newState);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(zoomSlider)) {
			view.setZoom(zoomSlider.getValue() / 100);
		}
	}

}
