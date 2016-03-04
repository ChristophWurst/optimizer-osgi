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
package at.christophwurst.optimize.optimizer;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public interface Optimizer {

	/**
	 * Get the name of this optimizer
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Start optimizing the given value
	 *
	 * @param val
	 */
	public void startOptimization(double val);

	/**
	 * Get current progress of optimization
	 *
	 * @return
	 */
	public int getProgress();

	/**
	 * Returns whether an optimization is running
	 *
	 * @return
	 */
	public boolean isRunning();

	/**
	 * Get the optimization result
	 *
	 * is only a valid value if the optimizer is not running
	 */
	public double getResult();

	/**
	 * Register a new PropertyChangedListener
	 *
	 * @param listener
	 */
	public void addPropertyChangedListener(PropertyChangeListener listener);

	/**
	 * unregister a PropertyChangedListener
	 *
	 * @param listener
	 */
	public void removePropertyChangedListener(PropertyChangeListener listener);

}
