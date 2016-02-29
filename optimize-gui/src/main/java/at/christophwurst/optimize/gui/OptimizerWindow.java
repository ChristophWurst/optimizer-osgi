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

import at.christophwurst.optimize.manager.Manager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class OptimizerWindow {

	private static final Logger LOG = Logger.getLogger(OptimizerWindow.class.getName());
	private final Manager manager;
	private Stage stage;
	private final HBox rootPane;
	private TextField input;
	private Button submitBtn;

	public OptimizerWindow(Manager manager) {
		this.manager = manager;
		rootPane = new HBox(getOptimizersPane(), getInputPane());
	}

	private Pane getOptimizersPane() {
		Label heading = new Label("Available optimizers");
		LOG.log(Level.INFO, "showing {0} optimizers", manager.getRegisteredOptimizers().size());
		List<String> optimizerNames = manager.getRegisteredOptimizers().stream().map(o -> {
			return o.getName();
		}).collect(Collectors.toList());
		LOG.log(Level.INFO, "showing {0} optimizers in GUI", optimizerNames.size());
		ObservableList<String> items = FXCollections.observableArrayList(optimizerNames);
		ListView<String> optimizers = new ListView<>(items);
		return new VBox(heading, optimizers);
	}
	
	private Pane getInputPane() {
		input = new TextField("16.3");
		submitBtn = new Button("optimize");
		submitBtn.setOnAction((ActionEvent t) -> {
			startOptimization();
		});
		return new HBox(input, submitBtn);
	}
	
	private void startOptimization() {
		double val = Double.parseDouble(input.getText());
		System.out.println("OPT " + val);
		manager.optimize(val);
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
