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
import at.christophwurst.optimize.optimizer.Optimizer;
import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
	private Manager manager;
	private Stage stage;
	private final Pane rootPane;
	private TextField input;
	private Button submitBtn;

	public OptimizerWindow() {
		rootPane = new VBox();
	}

	private void showManagerPane() {
		System.out.println("showing manager pane");
		rootPane.getChildren().clear();
		rootPane.getChildren().addAll(getInputPane(), getOptimizerPane());
		show();
	}

	private Pane getInputPane() {
		input = new TextField("16.3");
		submitBtn = new Button("optimize");
		submitBtn.setOnAction((ActionEvent t) -> {
			startOptimization();
		});
		submitBtn.setDisable(manager.isRunning());
		System.out.println("manager running: " + manager.isRunning());
		manager.addPropertyChangedListener((PropertyChangeEvent pce) -> {
			System.out.println("E manager running: " + manager.isRunning());
			submitBtn.setDisable(!(boolean) pce.getNewValue());
		});
		return new HBox(input, submitBtn);
	}

	private Pane getOptimizerPane() {
		Pane p = new FlowPane();
		p.setPadding(new Insets(10));
		for (Optimizer opt : manager.getRegisteredOptimizers()) {
			Label optLbl = new Label(opt.getName());
			ProgressIndicator prog = new ProgressIndicator(opt.getProgress() / 100f);
			opt.addPropertyChangedListener((PropertyChangeEvent pce) -> {
				if (pce.getPropertyName().equals("progress")) {
					int pro = (int) pce.getNewValue();
					prog.setProgress(pro / 100f);
				}
			});
			BorderPane elem = new BorderPane();
			elem.setTop(optLbl);
			elem.setCenter(prog);
			p.getChildren().add(elem);
		}
		return p;
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

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
		if (manager == null) {
			close();
		} else {
			showManagerPane();
		}
	}

}
