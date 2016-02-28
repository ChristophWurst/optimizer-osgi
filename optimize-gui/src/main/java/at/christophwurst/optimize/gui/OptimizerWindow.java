/*
 * Copyright (C) 2016 Christoph Wurst <christoph@winzerhof-wurst.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.christophwurst.optimize.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class OptimizerWindow {

	private Stage stage;
	private final VBox rootPane;

	public OptimizerWindow() {
		Button butt = new Button("HELLO, it's me");
		rootPane = new VBox(butt);
	}

	public void show() {
		if (stage == null) {
			stage = new Stage();
			stage.setScene(new Scene(rootPane, 500, 500));
			stage.setMinWidth(200);
			stage.setMinHeight(200);
			stage.setTitle("Optimizer");
		}
		stage.show();
	}

	public void close() {
		if (stage != null) {
			stage.close();
		}
	}

}
