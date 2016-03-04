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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Dictionary;
import java.util.Hashtable;
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
	private final PropertyChangeSupport changed;
	private final EventAdmin eventAdmin;
	private final Vector<Optimizer> optimizers;
	private final AtomicBoolean isRunning;

	public Manager(EventAdmin eventAdmin) {
		changed = new PropertyChangeSupport(this);
		this.eventAdmin = eventAdmin;
		this.optimizers = new Vector<>();
		this.isRunning = new AtomicBoolean(false);
	}

	public void optimize(double val) {
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
		boolean oldVal = isRunning.get();
		isRunning.set(val);
		changed.firePropertyChange("running", oldVal, val);
	}

	public Vector<Optimizer> getRegisteredOptimizers() {
		return optimizers;
	}

	public void registerOptimizer(Optimizer optimizer) {
		optimizers.add(optimizer);

		Dictionary props = new Hashtable();
		props.put("optimizer", optimizer);
		Event e = new Event("at/christophwurst/optimize/manager/optimizer/ADDED", props);
		eventAdmin.postEvent(e);

		LOG.log(Level.INFO, "Optimizer registered: {0}", optimizer.getName());
	}

	public void unregisterOptimizer(Optimizer optimizer) {
		optimizers.remove(optimizer);

		Dictionary props = new Hashtable();
		props.put("optimizer", optimizer);
		Event e = new Event("at/christophwurst/optimize/manager/optimizer/REMOVED", props);
		eventAdmin.postEvent(e);

		LOG.log(Level.INFO, "Optimizer unregistered: {0}", optimizer.getName());
	}

	public void addPropertyChangedListener(PropertyChangeListener listener) {
		changed.addPropertyChangeListener(listener);
	}

	public void removePropertyChangedListener(PropertyChangeListener listener) {
		changed.removePropertyChangeListener(listener);
	}

}
