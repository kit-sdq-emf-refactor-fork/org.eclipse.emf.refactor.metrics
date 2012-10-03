package org.eclipse.emf.refactor.metrics.ocl.core;

import org.eclipse.emf.refactor.metrics.generator.core.MetricInfo;

public class OCLMetricInfo extends MetricInfo {

	private String expression;
	
	public OCLMetricInfo(String name, String id, String description,
			String metamodel, String context, String project, 
			String jar, String importPackage, String expression) {
		super(name, id, description, metamodel, context, project, jar, importPackage);
		this.expression = expression;
	}
	
	public String getExpression(){
		return expression;
	}

}
