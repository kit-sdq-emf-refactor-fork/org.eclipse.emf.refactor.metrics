package org.eclipse.emf.refactor.metrics.henshin.managers;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.refactor.metrics.generator.managers.GenerationManager;
import org.eclipse.emf.refactor.metrics.generator.managers.XMLPluginFileManager;
import org.eclipse.emf.refactor.metrics.henshin.Activator;
import org.eclipse.emf.refactor.metrics.henshin.core.HenshinMetricInfo;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class HenshinGenerationManager extends GenerationManager {
	
	private static final String HENSHIN_TEMPLATE_CLASS_NAME = "HenshinCalculateClassTemplate";
	private static HenshinGenerationManager instance;
	
	private HenshinGenerationManager() {
		templateDirectory = setTemplateDirectory();
		classpathEntries = setClassPathEntries();
		System.out.println("HenshinGenerationManager initialized!");
	}
	
	public static HenshinGenerationManager getInstance() {
		if (instance == null) {
			instance = new HenshinGenerationManager();
		}
		return instance;
	}
	
	protected List<IClasspathEntry> setClassPathEntries() {
		List<IClasspathEntry> cpe = super.setClassPathEntries();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
	    // add org.eclipse.emf.refactor.metrics.henshin to class path
	    String version = (String) bundle.getHeaders().get(BUNDLEVERSION);
	    cpe.add(JavaCore.newLibraryEntry(new Path(PLUGINSPATH + 
	    		Activator.PLUGIN_ID + "_" + version + ".jar"), null, null));
	    return cpe;
	}
	
	public static void createNewMetric(IProgressMonitor monitor,
			HenshinMetricInfo metricInfo, IProject targetProject) {
		System.out.println(metricInfo);
		HenshinDependenciesManager.updateDependencies(metricInfo);
		createCalculateClass(monitor, metricInfo);		
		XMLPluginFileManager.createMetricEntry(metricInfo.getProjectPath(), 
				metricInfo.getName(), metricInfo.getId(), 
				metricInfo.getDescription(), metricInfo.getMetamodel(), 
				metricInfo.getContext(), metricInfo.getPackage()+"."+metricInfo.getClassName());
		try {
			targetProject.refreshLocal(IProject.DEPTH_INFINITE, monitor);
			targetProject.refreshLocal(IProject.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private static void createCalculateClass(IProgressMonitor monitor, HenshinMetricInfo metricInfo) {
		String generatedCode = "";
		String templateName = HENSHIN_TEMPLATE_CLASS_NAME;
		try {
			generatedCode = generateCode(monitor, templateName, metricInfo);
		    saveCode(monitor, generatedCode, metricInfo);
		} catch (JETException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
