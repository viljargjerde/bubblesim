/*
 * 
 */
package inf101v22.bubblesim.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import inf101v22.unit.Pressure;
import inf101v22.unit.Temperature;
import inf101v22.unit.Units;

/**
 * The Class ParameterChanger contains button to pause, and elements to change
 * parameters in an electrodeModel
 */
public class ParameterChanger extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	ElectrodeViewable electrodeModel;

	JButton pause;

	JComboBox<String> choiceSelect;

	JFormattedTextField inputField;

	/**
	 * Instantiates a new parameter changer.
	 *
	 * @param electrodeModel the electrode model
	 */
	public ParameterChanger(ElectrodeViewable electrodeModel) {
		this.electrodeModel = electrodeModel;
		this.setLayout(new FlowLayout());
		String[] choices = { "Pressure", "Temperature", "Distance from center", "RPM" };
		this.choiceSelect = new JComboBox<>(choices);
		this.add(choiceSelect);
		choiceSelect.addActionListener(this);
		NumberFormat format = DecimalFormat.getInstance();
		format.setRoundingMode(RoundingMode.HALF_UP);
		this.inputField = new JFormattedTextField(format);
		inputField.setColumns(10);
		this.add(inputField);
		inputField.addActionListener(this);
		this.pause = new JButton("Start");
		this.pause.setFocusable(false);
		this.pause.addActionListener(this);
		this.add(pause);
		updateInputField(this.electrodeModel.getPressure().value(Units.Bar));
	}

	/**
	 * Update input field to the given value
	 *
	 * @param newValue the value that inputFields should show
	 */
	private void updateInputField(double newValue) {
		inputField.setValue((Double.valueOf(newValue)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object actionSource = e.getSource();
		if (actionSource.equals(pause)) {
			electrodeModel.setPaused(!electrodeModel.getPaused());
			if (electrodeModel.getPaused()) {
				pause.setText("Unpause");
			} else {
				pause.setText("Pause");
			}

		} else if (actionSource.equals(inputField)) {
			String action = (String) choiceSelect.getSelectedItem();
			if (inputField.getValue() == null) {
				return;
			}
			Double value;
			Object inputValue = inputField.getValue();
			if (inputValue instanceof Long) {

				value = Long.valueOf((Long) (inputValue)).doubleValue();
			} else {
				value = (Double) (inputValue);
			}
			switch (action) {
			case "Pressure":
				Pressure pressure = electrodeModel.getPressure();
				pressure.setValue(value, Units.Bar);
				electrodeModel.setPressure(pressure);
				break;
			case "Temperature":
				Temperature temp = electrodeModel.getTemperature();
				temp.setValue(value, Units.C);
				break;
			case "Distance from center":
				electrodeModel.setDistanceFromCenter(value);
				break;
			case "RPM":
				electrodeModel.setRPM(value);
				break;
			}
		} else if (actionSource.equals(choiceSelect)) {
			String action = (String) choiceSelect.getSelectedItem();
			switch (action) {
			case "Pressure":
				Pressure pressure = electrodeModel.getPressure();
				updateInputField(pressure.value(Units.Bar));
				break;
			case "Temperature":
				Temperature temp = electrodeModel.getTemperature();
				updateInputField(temp.value(Units.C));
				break;
			case "Distance from center":
				updateInputField(electrodeModel.getDistanceFromCenter());
				break;
			case "RPM":
				updateInputField(electrodeModel.getRPM());
				break;
			}

		}
	}
}