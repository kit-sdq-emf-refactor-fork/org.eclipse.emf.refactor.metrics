package org.eclipse.emf.refactor.metrics.managers;

import java.util.LinkedList;

import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.core.MetricLoader;
import org.eclipse.emf.refactor.metrics.interfaces.IOperation;
import org.eclipse.emf.refactor.metrics.operations.Operations;

public class MetricManager {
	
	private static LinkedList<Metric> allMetrics = null;
	
	private static MetricManager instance;
	
	private MetricManager() {
		allMetrics = MetricLoader.loadMetrics();
		System.out.println("MetricManager initialized!");
	}
	
	public static MetricManager getInstance() {
		if (instance == null) {
			instance = new MetricManager();
		}
		return instance;
	}
	
	public static LinkedList<Metric> getAllMetrics() {
		if (allMetrics == null) {
			allMetrics = MetricLoader.loadMetrics();
		}
		return allMetrics;
	}	
	
	/**
	 * Returns a LinkedList containing all selected metrics for a given project that are
	 * defined for the given meta model and the given context.
	 * 
	 * @param metamodel
	 * @param context
	 * 
	 * @return filteredMetrics
	 */
	public static LinkedList<Metric> getFilteredMetrics(String metamodel, String context) {
		LinkedList<Metric> filteredMetrics = new LinkedList<Metric>();
		for (Metric metric : getAllMetrics()) {
			if (metric.getMetamodel().equals(metamodel) 
					&& metric.getContext().equals(context)) {
				filteredMetrics.add(metric);
			}
		}
		return filteredMetrics;
	}
	
	/**
	 * Returns the operation class with the given name, 
	 * which implements the <i>IOperation</i> interface.
	 * 
	 * @param operation name
	 * 
	 * @return operation class
	 */
	public static IOperation getOperation(String name){
		return Operations.getOperation(name);
	}
	
	/**
	 * Returns an <i>String</i> array containing the names of all supported operations.
	 * 
	 * @return names of all supported operations.
	 */
	public static String[] getOperationNames(){
		return Operations.getNames();
	}

}
