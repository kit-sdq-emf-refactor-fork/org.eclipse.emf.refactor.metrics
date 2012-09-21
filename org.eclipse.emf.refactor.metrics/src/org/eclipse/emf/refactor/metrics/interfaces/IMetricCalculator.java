package org.eclipse.emf.refactor.metrics.interfaces;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * This interface defines the Methods needed to calculate a metric.
 * All metric calculate classes must implement this interface.
 * 
 * @author Pawel Stepien
 */
public interface IMetricCalculator {

	/**
	 * Sets the context for the metric calculation.
	 * 
	 * @param context
	 */
	abstract public void setContext(List<EObject> context);
	
	/**
	 * Returns the result of the metric calculation on a given context element.
	 * 
	 * @return metric result
	 */
	abstract public double calculate();
	
}
