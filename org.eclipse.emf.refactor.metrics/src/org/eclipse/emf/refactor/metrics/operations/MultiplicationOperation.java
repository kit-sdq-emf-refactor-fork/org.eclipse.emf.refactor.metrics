package org.eclipse.emf.refactor.metrics.operations;

import org.eclipse.emf.refactor.metrics.interfaces.IOperation;

public class MultiplicationOperation implements IOperation {

	@Override
	public double calculate(double a, double b) {
		return a*b;
	}

}
