package org.eclipse.emf.refactor.metrics.generator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

public class GenerationManager {
	
	private static GenerationManager instance;
	
	private GenerationManager() {
		System.out.println("GenerationManager initialized!");
	}
	
	public static GenerationManager getInstance() {
		if (instance == null) {
			instance = new GenerationManager();
		}
		return instance;
	}

	public static void createNewMetric(IProgressMonitor monitor,
			MetricInfo metricInfo, IProject targetProject) {
		System.out.println("Here we go...");
	}

}
