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
package at.christophwurst.optimize.manager;

import at.christophwurst.optimize.optimizer.Optimizer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class ManagerActivator implements BundleActivator {

	private Manager manager;
	private ServiceTracker<Optimizer, Optimizer> optimizerTracker;

	@Override
	public void start(BundleContext bc) throws Exception {
		System.out.println("Starting manager");

		ServiceReference<EventAdmin> ref = bc.getServiceReference(EventAdmin.class);
		if (ref != null) {
			EventAdmin eventAdmin = bc.getService(ref);
			
			manager = new Manager(eventAdmin);

			registerManagerService(bc);
			startTrackingOptimzers(bc);

			System.out.println("Manager started");
		} else {
			System.err.println("Cannot start optimize-manager because EventAdmin has not been loaded");
		}
	}

	@Override
	public void stop(BundleContext bc) throws Exception {
		// Nothing to do
	}

	private void registerManagerService(BundleContext bc) {
		bc.registerService(Manager.class, manager, null);
	}

	private void startTrackingOptimzers(BundleContext bc) {
		optimizerTracker = new ServiceTracker<>(bc, Optimizer.class, new OptimizerTrackerCustomizer(bc, manager));
		optimizerTracker.open();
	}

}
