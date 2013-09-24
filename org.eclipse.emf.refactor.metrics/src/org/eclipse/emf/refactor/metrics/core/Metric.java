package org.eclipse.emf.refactor.metrics.core;

import java.util.LinkedList;

import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.metrics.managers.MetricManager;

/**
 * This class provides a container for the metrics.
 * 
 * @author Pawel Stepien
 */
public class Metric implements Comparable<Metric> {

	private String id;
	private String name;
	private String description;
	private String metamodel;
	private String context;
	private IMetricCalculator calculateClass;

	/**
	 * The constructor that creates a new <i>Metric</i> object.
	 * 
	 * @param name
	 * @param description
	 * @param metamodel
	 * @param context
	 * @param valueType
	 * @param calculateClass
	 * @param id
	 */
	public Metric(String name, String description, String metamodel,
			String context, IMetricCalculator calculateClass, String id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.metamodel = metamodel;
		this.context = context;
		this.calculateClass = calculateClass;
	}

	/**
	 * Returns the ID of the metric.
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the name of the metric.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the metric.
	 * 
	 * @return description
	 */
	public String getDescription() {
		if (description == null)
			return " - ";
		return description;
	}

	/**
	 * Returns the metamodel of the metric.
	 * 
	 * @return metamodel
	 */
	public String getMetamodel() {
		return metamodel;
	}

	/**
	 * Returns the the context of the metric.
	 * 
	 * @return context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Returns the <i>ICalculateClass</i> implementing class which contains the code
	 * for the metric calculation.
	 * 
	 * @return calculateClass
	 */
	public IMetricCalculator getCalculateClass() {
		return calculateClass;
	}
	
	public static Metric getMetricInstanceFromId(String id){
		LinkedList<Metric> allMetrics = MetricManager.getAllMetrics();
		for(Metric metric : allMetrics){
			if(metric.getId().equals(id))
				return metric;
		}
		return null;
	}

	@Override
	public int compareTo(Metric metric) {
		if(this.equals(metric)) {
			return 0;
		}
		if(this.getName().equals(metric.getName())) {
			return 0;
		} else {
		    return this.getName().compareTo(metric.getName());	
		}
	}
}
