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
package at.christophwurst.optimizer.slow;

import at.christophwurst.optimize.optimizer.Optimizer;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class SlowOptimizer implements Optimizer {

	@Override
	public void startOptimization(double val) {
		System.out.println("slowly optimizing " + val + "…");
	}

}
