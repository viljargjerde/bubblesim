package inf101v22.bubblesim;

import inf101v22.bubblesim.controller.Controller;
import inf101v22.bubblesim.model.Model;
import inf101v22.bubblesim.view.View;

public class Main {
	public static String[] args;

	public static void main(String[] args) {
		Main.args = args;

		int totFrames; // Total frames before the program shuts down
		try {
			totFrames = Integer.parseInt(args[0]);
		} catch (Exception e) {
			totFrames = -1;
		}
		boolean headless = false;
//		System.setProperty("sun.java2d.opengl", "true");
		Model model = new Model();
		for (String parameterCode : args) {
			if (parameterCode.equals("headless")) {
				headless = true;
				continue;
			}
			if (parameterCode.equals("static") || parameterCode.equals("dynamic") || parameterCode.toLowerCase().equals("rk4")) {
				continue;
			}

			model.setParameter(parameterCode);
		}
		View view = new View(model, headless);
		Controller controller = new Controller(view, model, totFrames);
		controller.startTimer();
	}
}
