package org.eclipse.emf.refactor.metrics;

import java.util.LinkedList;

public class MetricManager {
	
	private static LinkedList<Metric> allMetrics=null;
	
	
	public static LinkedList<Metric> getAllMetrics() {
		if (allMetrics == null) {
			allMetrics = MetricLoader.loadMetrics();
		}
		return allMetrics;
	}	

}
