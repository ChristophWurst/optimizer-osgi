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
import at.christophwurst.optimize.utils.JavaFxUtils;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class ManagerTrackerCustomizer implements ServiceTrackerCustomizer<Manager, Manager> {

	private final OptimizerWindow window;
	private final BundleContext bc;

	public ManagerTrackerCustomizer(BundleContext bc, OptimizerWindow window) {
		this.bc = bc;
		this.window = window;
	}

	@Override
	public Manager addingService(ServiceReference<Manager> sr) {
		Manager mgr = bc.getService(sr);
		try {
			JavaFxUtils.runAndWait(() -> {
				window.setManager(mgr);
			});
		} catch (InterruptedException | ExecutionException ex) {
			Logger.getLogger(ManagerTrackerCustomizer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return mgr;
	}

	@Override
	public void modifiedService(ServiceReference<Manager> sr, Manager mgr) {
		try {
			JavaFxUtils.runAndWait(() -> {
				window.setManager(mgr);
			});
		} catch (InterruptedException | ExecutionException ex) {
			Logger.getLogger(ManagerTrackerCustomizer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void removedService(ServiceReference<Manager> sr, Manager mgr) {
		try {
			JavaFxUtils.runAndWait(() -> {
				window.setManager(null);
			});
		} catch (InterruptedException | ExecutionException ex) {
			Logger.getLogger(ManagerTrackerCustomizer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
