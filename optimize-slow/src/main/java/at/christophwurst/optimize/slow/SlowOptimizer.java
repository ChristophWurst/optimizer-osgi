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

import at.christophwurst.optimize.optimizer.SimpleOptimizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class SlowOptimizer extends SimpleOptimizer {

	@Override
	public String getName() {
		return "Slow Optimizer";
	}

	@Override
	protected Thread getWorkerThread(double val) {
		return new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				setProgress(i);
				try {
					Thread.sleep(200);
				} catch (InterruptedException ex) {
					System.err.println("slow optimizer thread interrupted exception");
					Logger.getLogger(SlowOptimizer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			setRunning(false);
			worker = null;
		});
	}

}
