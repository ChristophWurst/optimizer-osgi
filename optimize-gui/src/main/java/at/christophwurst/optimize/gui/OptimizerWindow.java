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
import at.christophwurst.optimize.utils.JavaFxUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import org.osgi.service.event.Event;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class OptimizerWindow {

	private class OptimizerElement extends BorderPane {

		private final Optimizer optimizer;
		private final Label name;
		private final Label result;
		private final ProgressIndicator progress;
		private PropertyChangeListener changeListener;

		public OptimizerElement(Optimizer optimizer) {
			super();
			this.optimizer = optimizer;
			name = new Label(optimizer.getName());
			result = new Label(getResultText());
			result.setAlignment(Pos.CENTER);
			progress = new ProgressIndicator(getProgress());
			super.setTop(name);
			super.setCenter(progress);
			super.setBottom(result);

			registerEvents();
		}

		private String getResultText() {
			return "Result: "
				+ (optimizer.isRunning() ? "–" : String.format("%.2f", optimizer.getResult()));
		}

		private float getProgress() {
			return optimizer.getProgress() / 100f;
		}

		private void registerEvents() {
			changeListener = (PropertyChangeEvent pce) -> {
				JavaFxUtils.runLater(() -> {
					progress.setProgress(getProgress());
					result.setText(getResultText());
				});
			};
			optimizer.addPropertyChangedListener(changeListener);
		}

		public void unregisterEvents() {
			optimizer.removePropertyChangedListener(changeListener);
		}

	}

	private static final Logger LOG = Logger.getLogger(OptimizerWindow.class.getName());
	private Manager manager;
	private final Map<Optimizer, OptimizerElement> optimizers;
	private Stage stage;
	private final Pane rootPane;
	private Pane optimizersPane;
	private TextField input;
	private Button submitBtn;

	public OptimizerWindow() {
		optimizers = new HashMap<>();
		rootPane = new VBox();
	}

	private void showManagerPane() {
		rootPane.getChildren().clear();

		optimizersPane = new FlowPane();
		optimizersPane.setPadding(new Insets(10));

		manager.getRegisteredOptimizers().stream().forEach(this::addOptimizer);

		rootPane.getChildren().addAll(getInputPane(), optimizersPane);
		show();
	}

	private Pane getInputPane() {
		input = new TextField("16.3");
		submitBtn = new Button("optimize");
		submitBtn.setOnAction((ActionEvent t) -> {
			startOptimization();
		});
		submitBtn.setDisable(manager.isRunning());
		return new HBox(input, submitBtn);
	}

	private void startOptimization() {
		double val = Double.parseDouble(input.getText());
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
			optimizersPane.getChildren().clear();
			optimizers.forEach((opt, elem) -> {
				elem.unregisterEvents();
			});
			optimizers.clear();
		} else {
			showManagerPane();
		}
	}

	private void addOptimizer(Optimizer optimizer) {
		if (optimizersPane == null || optimizers.containsKey(optimizer)) {
			// Nothing to do
			return;
		}

		OptimizerElement elem = new OptimizerElement(optimizer);
		optimizers.put(optimizer, elem);
		optimizersPane.getChildren().add(elem);
	}

	private void removeOptimizer(Optimizer optimizer) {
		if (optimizersPane == null || !optimizers.containsKey(optimizer)) {
			// Nothing to do
			return;
		}

		OptimizerElement elem = optimizers.remove(optimizer);
		elem.unregisterEvents();
		optimizersPane.getChildren().remove(elem);
	}

	void processEvent(Event event) {
		Optimizer optimizer;
		switch (event.getTopic()) {
			case "at/christophwurst/optimize/manager/STARTED":
				input.setDisable(true);
				submitBtn.setDisable(true);
				break;
			case "at/christophwurst/optimize/manager/FINISHED":
				input.setDisable(false);
				submitBtn.setDisable(false);
				break;
			case "at/christophwurst/optimize/manager/optimizer/ADDED":
				optimizer = (Optimizer) event.getProperty("optimizer");
				addOptimizer(optimizer);
				break;
			case "at/christophwurst/optimize/manager/optimizer/REMOVED":
				optimizer = (Optimizer) event.getProperty("optimizer");
				removeOptimizer(optimizer);
				break;
		}
	}

}
