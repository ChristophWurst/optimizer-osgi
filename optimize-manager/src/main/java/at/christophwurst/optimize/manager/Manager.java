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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class Manager {

	private static final Logger LOG = Logger.getLogger(Manager.class.getName());
	private final EventAdmin eventAdmin;
	private final Vector<Optimizer> optimizers;
	private final Map<Optimizer, PropertyChangeListener> changeListeners;
	private final AtomicBoolean isRunning;

	public Manager(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
		optimizers = new Vector<>();
		changeListeners = new HashMap<>();
		isRunning = new AtomicBoolean(false);
	}

	public void optimize(double val) {
		Event e = new Event("at/christophwurst/optimize/manager/STARTED", new HashMap<>());
		eventAdmin.postEvent(e);

		setRunning(true);
		for (Optimizer opt : (Vector<Optimizer>) optimizers.clone()) {
			System.out.println("starting optimization of " + val + " on " + opt.getName());
			opt.startOptimization(val);
		}
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	private void setRunning(boolean val) {
		isRunning.set(val);
	}

	public Vector<Optimizer> getRegisteredOptimizers() {
		return optimizers;
	}

	private void checkFinishedOptimizers() {
		Vector<Optimizer> opts = (Vector<Optimizer>) optimizers.clone();
		boolean allFinished = opts.stream().allMatch((opt) -> {
			return !opt.isRunning();
		});
		if (allFinished) {
			Event e = new Event("at/christophwurst/optimize/manager/FINISHED", new HashMap<>());
			eventAdmin.postEvent(e);
		}
	}

	public void registerOptimizer(Optimizer optimizer) {
		optimizers.add(optimizer);

		Dictionary props = new Hashtable();
		props.put("optimizer", optimizer);
		Event e = new Event("at/christophwurst/optimize/manager/optimizer/ADDED", props);
		eventAdmin.postEvent(e);

		PropertyChangeListener listener = (PropertyChangeEvent pce) -> {
			checkFinishedOptimizers();
		};
		optimizer.addPropertyChangedListener(listener);
		changeListeners.put(optimizer, listener);

		LOG.log(Level.INFO, "Optimizer registered: {0}", optimizer.getName());
	}

	public void unregisterOptimizer(Optimizer optimizer) {
		optimizers.remove(optimizer);

		PropertyChangeListener listener = changeListeners.remove(optimizer);
		optimizer.removePropertyChangedListener(listener);

		Dictionary props = new Hashtable();
		props.put("optimizer", optimizer);
		Event e = new Event("at/christophwurst/optimize/manager/optimizer/REMOVED", props);
		eventAdmin.postEvent(e);

		LOG.log(Level.INFO, "Optimizer unregistered: {0}", optimizer.getName());
	}

}
