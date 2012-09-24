package org.eclipse.emf.refactor.metrics.configuration.core;

import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.managers.MetricManager;

/**
 * This class provides a container for the project-specific metric configurations.
 * 
 * @author Pawel Stepien
 */
public class Configuration {

	private IProject project;
	private boolean[] selection = null;

	/**
	 * Constructor which creates a new configuration for a given project.
	 * 
	 * @param project
	 */
	public Configuration(IProject project) {
		this.project = project;
	}

	/**
	 * Sets the metric selection for this project.
	 * This method awaits a boolean array as parameter.
	 * 
	 * @param selection
	 */
	public void setSelection(boolean[] selection) {
		this.selection = selection;
	}
	
	/**
	 * Returns the the exact date of the calculation.
	 * This method awaits a <i>LinkedList</i> containing the ID's of the selected metrics 
	 * as parameter.
	 * 
	 * @param idList
	 */
	public void setSelection(LinkedList<String> idList) {
		this.selection = getSelection(idList);
	}

	/**
	 * Returns the the project for that this configuration has been made.
	 * 
	 * @return project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * Returns a <i>LinkedList</i> containing all metrics selected for this project..
	 * 
	 * @return selectedMetrics
	 */
	public LinkedList<Metric> getSelectedMetrics() {
		if(selection==null)
			return new LinkedList<Metric>();
		final int nuberOfMetrics = MetricManager.getAllMetrics().size();
		LinkedList<Metric> selectedMetrics = new LinkedList<Metric>();
		for (int index = 0; index < nuberOfMetrics; index++) {
			if (selection[index] == true)
				selectedMetrics.add(MetricManager.getAllMetrics().get(index));
		}
		return selectedMetrics;
	}
	
	private boolean[] getSelection(LinkedList<String> idList){
		final LinkedList<Metric> allMetrics = MetricManager.getAllMetrics();
		boolean[] selection = new boolean[allMetrics.size()];
		for(int index=0; index<selection.length; index++)
			selection[index]=false;
		int index;
		for(String id : idList){
			index = isInList(id, allMetrics);
			if (index>=0){
				selection[index]=true;
			}
		}
		return selection;
	}
	
	private int isInList(String id, LinkedList<Metric> metrics){
		for(int index=0; index<metrics.size(); index++){
			if(metrics.get(index).getId().equals(id))
				return index;
		}	
		return -1;
	}
	
}
