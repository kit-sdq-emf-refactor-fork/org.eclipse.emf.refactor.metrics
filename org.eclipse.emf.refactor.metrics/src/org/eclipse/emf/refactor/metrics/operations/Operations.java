package org.eclipse.emf.refactor.metrics.operations;

import org.eclipse.emf.refactor.metrics.interfaces.IOperation;


public class Operations {
	
	private final static String[] operationNames = {"Sum","Multiplication", "Subtraction", "Division"};
	
	/**
	 * Returns the names of all supported operations.
	 * 
	 * @return operation names
	 */
	public static String[] getNames(){
		return operationNames;
	}
	
	/**
	 * Returns the operation Class with the given name, 
	 * which implements the IOperation interface.
	 * 
	 * @param operation name
	 * 
	 * @return operation Class
	 */
	public static IOperation getOperation(String name){
		if(name.equals("Sum"))
			return new SumOperation();
		if(name.equals("Multiplication"))
			return new MultiplicationOperation();
		if(name.equals("Subtraction"))
			return new SubtractionOperation();
		if(name.equals("Division"))
			return new DivisionOperation();
		return null;
	}
	
	/**
	 * Returns the operation name of the IOperation object passed to the function
	 * 
	 * @param operation name
	 * 
	 * @return operation Class
	 */
	
	public static String getOperationName(IOperation operation){
		if(operation instanceof SumOperation)
			return "Sum";
		if(operation instanceof DivisionOperation)
			return "Division";
		if(operation instanceof MultiplicationOperation)
			return "Multiplication";
		if(operation instanceof SubtractionOperation)
			return "Subtraction";
		return null;
		
	}
	
}
