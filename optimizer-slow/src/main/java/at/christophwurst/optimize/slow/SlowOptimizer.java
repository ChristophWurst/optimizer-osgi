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
package at.christophwurst.optimize.slow;

import at.christophwurst.optimize.optimizer.Optimizer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class SlowOptimizer implements Optimizer {

	protected final PropertyChangeSupport changer;
	protected final AtomicInteger progress;
	protected final AtomicBoolean running;
	protected Thread worker;

	public SlowOptimizer() {
		changer = new PropertyChangeSupport(this);
		progress = new AtomicInteger(0);
		running = new AtomicBoolean(false);
	}

	@Override
	public String getName() {
		return "Slow Optimizer";
	}

	@Override
	public void startOptimization(double val) {
		setRunning(true);
		worker = new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				setProgress(i);
				try {
					Thread.sleep(200);
				} catch (InterruptedException ex) {
					Logger.getLogger(SlowOptimizer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			setRunning(false);
		});
		worker.start();
	}

	@Override
	public boolean isRunning() {
		return running.get();
	}

	private void setRunning(boolean val) {
		boolean oldVal = running.get();
		running.set(val);
		changer.firePropertyChange("running", oldVal, val);
	}

	@Override
	public int getProgress() {
		return progress.get();
	}

	private void setProgress(int val) {
		int oldVal = progress.get();
		progress.set(val);
		changer.firePropertyChange("progress", oldVal, val);
	}

	@Override
	public void addPropertyChangedListener(PropertyChangeListener listener) {
		changer.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangedListener(PropertyChangeListener listener) {
		changer.removePropertyChangeListener(listener);
	}

}
