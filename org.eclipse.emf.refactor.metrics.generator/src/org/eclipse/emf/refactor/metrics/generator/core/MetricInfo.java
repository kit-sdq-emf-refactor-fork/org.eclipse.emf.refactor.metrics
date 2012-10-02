package org.eclipse.emf.refactor.metrics.generator.core;

/**
 * This class provides a container for the information needed to generate a new metric. 
 * 
 * @author Pawel Stepien
 */
public class MetricInfo {

	private final static String PACKAGE_NAME = "org.eclipse.emf.refactor.metrics";
	private String name;
	private String description;
	private String project;
	private String context;
	private String metamodel;
	private String id;
	
	private String jar;
	private String importPackage;
	
	public MetricInfo(String name, String id, String description,
			String metamodel, String context, String project,
			String jar, String importPackage) {
		this.name = name;
		this.description = description;
		this.project = project;
		this.context = context;
		this.metamodel = metamodel;
		this.id = id;
		this.jar = jar;
		this.importPackage = importPackage;
	}
	
	/**
	 * Returns the name of the package in that the <i>ICalculateClass</i> of the metric
	 * will be generated.
	 * 
	 * @return packageName
	 */
	public String getPackage(){
		return PACKAGE_NAME;
	}
	
	/**
	 * Returns the ID of the metric that will be generated.
	 * 
	 * @return id
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Returns the name of the metric that will be generated.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name of the <i>ICalculateClass</i> for the metric that 
	 * will be generated.
	 * 
	 * @return className
	 */
	public String getClassName(){
		String head = ""+name.charAt(0);
		String tail = name.substring(1, (name.length()));
		return (head.toUpperCase()+tail).replace(" ", "");
	}
	
	/**
	 * Returns the description of the metric that will be generated.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the path to the target project for the metric that will be generated.
	 * 
	 * @return path
	 */
	public String getProjectPath() {
		return project;
	}
	
	/**
	 * Returns the name of the target project for the metric that will be generated.
	 * 
	 * @return projectName
	 */
	public String getProjectName() {
		int index = project.lastIndexOf("/");
		String result = project.substring(index+1);
		return result;
	}
	
	/**
	 * Returns the context of the metric that will be generated.
	 * 
	 * @return context
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * Returns the metamodel of the metric that will be generated.
	 * 
	 * @return metamodel
	 */
	public String getMetamodel() {
		return metamodel;
	}

	public CharSequence getJar() {
		return jar;
	}

	public void setJar(String jar) {
		this.jar = jar;
	}
	
	public void setImportPackage(String importPackage) {
		this.importPackage = importPackage;
	}
	
	public String getImportPackage() {
		return this.importPackage;
	}

	@Override
	public String toString() {
		return "MetricInfo [name=" + name + ", description=" + description
				+ ", project=" + project + ", context=" + context
				+ ", metamodel=" + metamodel + ", id=" + id + ", jar=" + jar
				+ "]";
	}

}
