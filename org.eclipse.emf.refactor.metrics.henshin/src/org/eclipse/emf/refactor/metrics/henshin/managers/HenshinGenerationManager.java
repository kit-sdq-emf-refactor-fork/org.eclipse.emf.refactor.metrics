package org.eclipse.emf.refactor.metrics.henshin.managers;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.refactor.metrics.generator.managers.GenerationManager;
import org.eclipse.emf.refactor.metrics.generator.managers.XMLPluginFileManager;
import org.eclipse.emf.refactor.metrics.henshin.Activator;
import org.eclipse.emf.refactor.metrics.henshin.core.HenshinMetricInfo;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
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
//			MessageDialog.openError(null, null, "vor generateCode()");
			generatedCode = generateCode(monitor, templateName, metricInfo);
//			MessageDialog.openError(null, null, "nach generateCode()");
		    saveCode(monitor, generatedCode, metricInfo);
//		    MessageDialog.openError(null, null, "nach saveCode()");
		} catch (JETException e) {
//			MessageDialog.openError(null, null, e.getMessage());
			e.printStackTrace();
		} catch (CoreException e) {
//			MessageDialog.openError(null, null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected String setTemplateDirectory() {
		String directory = "";
		final Bundle bundle = Activator.getDefault().getBundle();
		try {
			directory = FileLocator.toFileURL(bundle.getEntry(TEMPLATE_DIR)).getFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
	
	protected static String generateCode(IProgressMonitor monitor, String template, HenshinMetricInfo metricInfo) {
		String templatePath = templateDirectory + template + TEMPLATE_FILE_EXTENSION;
		ClassLoader classLoader = metricInfo.getClass().getClassLoader();
		JETEmitter jetEmitter = new JETEmitter(templatePath, classLoader);
		jetEmitter.getClasspathEntries().addAll(classpathEntries);
		String result = "";
		IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
		try {
			result = jetEmitter.generate(subMonitor, new Object[] {metricInfo});
		} catch (JETException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

}
