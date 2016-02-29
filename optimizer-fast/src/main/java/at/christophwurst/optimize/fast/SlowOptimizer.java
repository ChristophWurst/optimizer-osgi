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
package at.christophwurst.optimize.fast;

import at.christophwurst.optimize.optimizer.Optimizer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
	protected Thread worker;

	public SlowOptimizer() {
		changer = new PropertyChangeSupport(this);
		this.progress = new AtomicInteger(0);
	}

	@Override
	public String getName() {
		return "Fast Optimizer";
	}

	@Override
	public void startOptimization(double val) {
		worker = new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				setProgress(i);
				try {
					Thread.sleep(20);
				} catch (InterruptedException ex) {
					Logger.getLogger(SlowOptimizer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		worker.start();
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
	public void addPropertyChanedListener(PropertyChangeListener listener) {
		changer.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChanedListener(PropertyChangeListener listener) {
		changer.removePropertyChangeListener(listener);
	}

}
