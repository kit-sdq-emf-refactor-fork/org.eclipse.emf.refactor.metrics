package org.eclipse.emf.refactor.metrics.henshin.core;																

import org.eclipse.emf.refactor.metrics.generator.core.MetricInfo;

/**
 * This class provides a container for the information needed to generate a new metric
 * using henshin transformations. 
 * This class extends the abstract <i>MetricInfo</i> class providing additional functionality. 
 * 
 * @author Pawel Stepien
 */
public class HenshinMetricInfo extends MetricInfo {

	private String transformation;
	
	public HenshinMetricInfo(String name, String id, String description,
			 String metamodel, String context, String project, 
			 String jar, String importPackage, String transformation) {
		super(name, id, description, metamodel, context, project, jar, importPackage);
		this.transformation = transformation;
	}

	/**
	 * Returns the path to the count-transformation file..
	 * 
	 * @return count-transformation file path
	 */
	public String getTransformationPath() {
		return transformation;
	}
	
	/**
	 * Returns the name of the count-transformation file.
	 * 
	 * @return count-transformation file name
	 */
	public String getTransformationName() {
		int index = transformation.lastIndexOf("/");
		String result = transformation.substring(index+1);
		return result;
	}	
}
