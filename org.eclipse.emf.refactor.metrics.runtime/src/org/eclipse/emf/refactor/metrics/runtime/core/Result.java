package org.eclipse.emf.refactor.metrics.runtime.core;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.runtime.managers.TimeStampManager;

/**
 * This class provides a container for the results of metric calculations.
 * 
 * @author Pawel Stepien
 */
public class Result{


	private Metric metric;
	private List<EObject> context;
	private String timeStamp;
	private double resultValue;

	/**
	 * The constructor which creates a new result object.
	 * 
	 * @param metric
	 * @param context
	 * @param resultValue
	 */
	public Result(Metric metric, List<EObject> context, double resultValue) {
		this.metric = metric;
		this.context = context;
		this.resultValue = resultValue;
		this.timeStamp = TimeStampManager.getTimeStamp();
	}
	
	/**
	 * Returns the the exact date of the calculation.
	 * 
	 * @return timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns the metric that has been calculated.
	 * 
	 * @return metric
	 */
	public Metric getMetric() {
		return metric;
	}

	/**
	 * Returns the the context of the calculation.
	 * 
	 * @return context
	 */
	public List<EObject> getContext() {
		return context;
	}

	/**
	 * Returns the value of the result of the calculation.
	 * 
	 * @return resultValue
	 */
	public double getResultValue() {
		return resultValue;
	}
}
