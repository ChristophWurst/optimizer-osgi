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
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class OptimizerTrackerCustomizer implements ServiceTrackerCustomizer<Optimizer, Optimizer> {

	private final BundleContext bc;
	private final Manager mgr;

	public OptimizerTrackerCustomizer(BundleContext bc, Manager mgr) {
		this.bc = bc;
		this.mgr = mgr;
	}

	@Override
	public Optimizer addingService(ServiceReference<Optimizer> sr) {
		Optimizer opt = bc.getService(sr);
		mgr.registerOptimizer(opt);
		System.out.println("Optimizer added to manager: " + opt.getName());
		return opt;
	}

	@Override
	public void modifiedService(ServiceReference<Optimizer> sr, Optimizer opt) {
		mgr.unregisterOptimizer(opt);
		mgr.registerOptimizer(opt);
		System.out.println("Optimizer modified: " + opt.getName());
	}

	@Override
	public void removedService(ServiceReference<Optimizer> sr, Optimizer opt) {
		mgr.unregisterOptimizer(opt);
		System.out.println("Optimizer removed from manager: " + opt.getName());
	}

}
