package org.eclipse.emf.refactor.metrics.generator.core;

import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IOperation;
import org.eclipse.emf.refactor.metrics.operations.Operations;


/**
 * This class provides a container for the information needed to generate a new metric
 * using two already existing metrics.
 * This class extends the abstract <i>MetricInfo</i> class providing additional functionality. 
 * 
 * @author Pawel Stepien
 */
public class CompositeMetricInfo extends MetricInfo  {

	private Metric firstMetric = null, secondMetric = null;
	private IOperation operation;
	
	/**
	 * The constructor that creates a new <i>CompositeMetricInfo</i> object.
	 * 
	 * @param name
	 * @param id
	 * @param description
	 * @param metamodel
	 * @param context
	 * @param valueType
	 * @param targetProject
	 * @param firstMetric
	 * @param secondMetric
	 * @param operation
	 * @param importPackage 
	 */
	public CompositeMetricInfo(String name, String id, String description, String metamodel, String context, String project, 
								Metric firstMetric, Metric secondMetric, IOperation operation, String jar, String importPackage) {
		super(name, id, description, metamodel, context, project, jar, importPackage);
		this.firstMetric = firstMetric;
		this.secondMetric = secondMetric;
		this.operation = operation;
	}

	/**
	 * Returns the first metric for the composition.
	 * 
	 * @return first metric
	 */
	public Metric getFirstMetric() {
		return firstMetric;
	}

	/**
	 * Returns the second metric for the composition.
	 * 
	 * @return second metric
	 */
	public Metric getSecondMetric() {
		return secondMetric;
	}

	/**
	 * Returns the operation for the metrics composition.
	 * 
	 * @return operation
	 */
	public IOperation getOperation() {
		return operation;
	}
	
	
	/**
	 * Returns the operationname for the metrics composition.
	 * 
	 * @return operationname
	 */
	public String getOperationName(){
		return Operations.getOperationName(operation);
	}

	@Override
	public String toString() {
		return "CompositeMetricInfo [firstMetric=" + firstMetric
				+ ", secondMetric=" + secondMetric + ", operation=" + operation
				+ ", getPackage()=" + getPackage() + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getClassName()="
				+ getClassName() + ", getDescription()=" + getDescription()
				+ ", getProjectPath()=" + getProjectPath()
				+ ", getProjectName()=" + getProjectName() + ", getContext()="
				+ getContext() + ", getMetamodel()=" + getMetamodel()
				+ ", getJar()=" + getJar() + "]";
	}

	
}
