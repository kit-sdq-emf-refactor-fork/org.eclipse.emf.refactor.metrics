package org.eclipse.emf.refactor.metrics.configuration.managers;

import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.refactor.metrics.configuration.core.Configuration;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.managers.MetricManager;


/**
 * This class is the main controller. It provides Methods needed to coordinate all 
 * EMFMetrics system tasks. 
 * 
 * @author Pawel Stepien
 */
public class ConfigurationManager {

	private static LinkedList<Configuration> configurations;
	private static LinkedList<Metric> allMetrics = null;
	
	private static ConfigurationManager instance;
	
	private ConfigurationManager() {
		configurations = new LinkedList<Configuration>();
		allMetrics = MetricManager.getAllMetrics();
		System.out.println("ConfigurationManager initialized!");
	}
	
	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		return instance;
	}
	
	private static LinkedList<Metric> getAllMetrics() {
		if (allMetrics == null)
			allMetrics = MetricManager.getAllMetrics();
		return allMetrics;
	}		
	
	/**
	 * Saves the actual Metric configuration for a given project into a File.
	 * 
	 * @param project
	 */
	public static void saveConfiguration(IProject project) {
		XMLProjectFileManager.saveConfiguration(getConfiguration(project));
	}
	
	private static Configuration loadConfiguration(IProject project) {
		return XMLProjectFileManager.loadConfiguration(project);
	}
	
	/**
	 * Returns a LinkedList containing all selected metrics for a given project.
	 * 
	 * @param project
	 * 
	 * @return selectedMetrics
	 */
	public static LinkedList<Metric> getSelectedMetrics(IProject project){
		Configuration configuration = getConfiguration(project);
		return configuration.getSelectedMetrics();
	}
	
	/**
	 * Saves a metric selection for a given project.
	 * 
	 * @param project
	 * @param metrics selection
	 */
	public static void setConfiguration(IProject project, boolean[] selection){
		Configuration configuration = getConfiguration(project);
		configuration.setSelection(selection);
	}

	public static Configuration getConfiguration(IProject project) {
		Configuration configuration = null;
		if(! configurations.isEmpty()) {
			for (Configuration tempConfiguration : configurations) {
				if (tempConfiguration.getProject().equals(project))
					configuration = tempConfiguration;
			}
		}
		if(configuration == null) {
			configuration = loadConfiguration(project);
		}
		if(configuration == null){
			configuration = new Configuration(project);	
			LinkedList<String> metricIdList = new LinkedList<String>();
			if (allMetrics == null) getAllMetrics();
			for (Metric metric : allMetrics) {
				metricIdList.add(metric.getId());
				System.out.println("add: " + metric.getId());
			}
			configuration.setSelection(metricIdList);
		}
		configurations.add(configuration);
		return configuration;
	}

}
