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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class Manager {

	private static final Logger LOG = Logger.getLogger(Manager.class.getName());
	private final Vector<Optimizer> optimizers;

	public Manager() {
		this.optimizers = new Vector<>();
	}

	public void registerOptimizer(Optimizer optimizer) {
		optimizers.add(optimizer);
		LOG.log(Level.INFO, "Optimizer registered: {0}", optimizer.getName());
	}

	public void unregisterOptimizer(Optimizer optimizer) {
		optimizers.remove(optimizer);
		LOG.log(Level.INFO, "Optimizer unregistered: {0}", optimizer.getName());
	}

}
