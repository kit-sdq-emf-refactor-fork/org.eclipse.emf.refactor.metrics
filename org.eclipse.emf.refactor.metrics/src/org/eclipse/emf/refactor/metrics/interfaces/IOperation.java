package org.eclipse.emf.refactor.metrics.interfaces;

public interface IOperation {
	
	/**
	 * Returns the result of a mathematical operation with two parameters. 
	 *
	 * @param first parameter
	 * @param second parameter
	 *  
	 * @return result of the operation
	 */
	public double calculate(double a, double b);
	
}
