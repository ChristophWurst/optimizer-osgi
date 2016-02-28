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

import at.christophwurst.optimize.utils.JavaFxUtils;
import at.christophwurst.optimize.manager.Manager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class GuiActivator implements BundleActivator {

	private OptimizerWindow window;
	private Manager manager;
	private ServiceTracker optimizerTracker;

	@Override
	public void start(BundleContext bc) throws Exception {
		JavaFxUtils.initJavaFx();
		JavaFxUtils.runAndWait(() -> startUI(bc));
		
		optimizerTracker = new ServiceTracker(bc, Manager.class, null);
		optimizerTracker.open();
		manager = (Manager) optimizerTracker.getService();
		// TODO: hook up GUI
		System.out.println("GUI started and registered");
	}

	@Override
	public void stop(BundleContext bc) throws Exception {
		JavaFxUtils.runAndWait(() -> stopUI(bc));
		System.out.println("GUI stopped");
	}

	private void startUI(BundleContext bc) {
		window = new OptimizerWindow();
		window.show();
	}

	private void stopUI(BundleContext bc) {
		window.close();
	}

}
