package org.eclipse.emf.refactor.metrics.operations;

import org.eclipse.emf.refactor.metrics.interfaces.IOperation;

public class DivisionOperation implements IOperation {

	@Override
	public double calculate(double a, double b) {
		if (b == 0) return Double.NaN;
		return a/b;
	}

}