package org.eclipse.emf.refactor.metrics.ocl.managers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.refactor.metrics.generator.core.MetricInfo;
import org.eclipse.emf.refactor.metrics.generator.managers.XMLPluginFileManager;
import org.eclipse.emf.refactor.metrics.ocl.Activator;
import org.eclipse.emf.refactor.metrics.ocl.core.OCLMetricInfo;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class OCLGenerationManager {
	
	private static final String OCL_TEMPLATE_CLASS_NAME = "OCLCalculateClassTemplate";
	private static OCLGenerationManager instance;
	private static final  String TEMPLATE_DIR = "/templates";
	private static final String SOURCE_DIR = "/src/";
	private static final  String TEMPLATE_FILE_EXTENSION = ".javajet";
	private static final String JAVA_FILE_EXTENSION = ".java";
	private static final String PLUGINSPATH = Platform.getInstallLocation().getURL().getPath() + "plugins/";
	private static final String BUNDLEVERSION = "Bundle-Version";
	private static String templateDirectory;
	private static List<IClasspathEntry> classpathEntries;
	
	private OCLGenerationManager() {
		templateDirectory = setTemplateDirectory();
		classpathEntries = setClassPathEntries();
		System.out.println("OCLGenerationManager initialized!");
	}
	
	public static OCLGenerationManager getInstance() {
		if (instance == null) {
			instance = new OCLGenerationManager();
		}
		return instance;
	}
	
	protected List<IClasspathEntry> setClassPathEntries() {
		List<IClasspathEntry> cpe = new ArrayList<IClasspathEntry>();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		// add org.eclipse.emf.refactor.metrics.ocl to class path
	    String version = (String) bundle.getHeaders().get(BUNDLEVERSION);
	    cpe.add(JavaCore.newLibraryEntry(new Path(PLUGINSPATH + 
	    		Activator.PLUGIN_ID + "_" + version + ".jar"), null, null));
	    // add org.eclipse.emf.refactor.metrics to class path
	    bundle = Platform.getBundle(org.eclipse.emf.refactor.metrics.Activator.PLUGIN_ID);
	    version = (String) bundle.getHeaders().get(BUNDLEVERSION);
	    cpe.add(JavaCore.newLibraryEntry(new Path(PLUGINSPATH + 
	    		org.eclipse.emf.refactor.metrics.Activator.PLUGIN_ID + "_" + version + ".jar"), null, null));	    
		return cpe;
	}
	
	public static void createNewMetric(IProgressMonitor monitor,
			OCLMetricInfo metricInfo, IProject targetProject) {
		System.out.println(metricInfo);
		OCLDependenciesManager.updateDependencies(metricInfo);
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
	
	private static void createCalculateClass(IProgressMonitor monitor, OCLMetricInfo metricInfo) {
		String generatedCode = "";
		String templateName = OCL_TEMPLATE_CLASS_NAME;
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
	
	protected static String generateCode(IProgressMonitor monitor, String template, OCLMetricInfo metricInfo) {
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
	
	protected static void saveCode(IProgressMonitor monitor, String content, MetricInfo metricInfo) throws CoreException, JETException {
		IContainer container = findOrCreatePackage(monitor, metricInfo);
		if (container == null) {
			throw new JETException("Could not find or create container for package " + metricInfo.getPackage() + " in " + metricInfo.getProjectName());
		}
		IFile file = container.getFile(new Path(metricInfo.getClassName() + JAVA_FILE_EXTENSION));
		System.out.println("saving code into: "+file.getLocation());
		InputStream inputStream = new ByteArrayInputStream(content.getBytes());
		if (!file.exists()) {
			file.create(inputStream, false, monitor);
		} else {
			container.refreshLocal(1, monitor);
			file.setContents(inputStream, true, false, monitor);
		}
	}
	
	private static IContainer findOrCreatePackage(IProgressMonitor progressMonitor, MetricInfo metricInfo) throws CoreException {
		IPath outputPath = new Path(metricInfo.getProjectName() + SOURCE_DIR + metricInfo.getPackage().replace('.', '/'));
		IProgressMonitor subMonitor = new SubProgressMonitor(progressMonitor, 1);
		IPath localLocation = null; 
		IContainer container = CodeGenUtil.EclipseUtil.findOrCreateContainer(outputPath, true, localLocation, subMonitor);
		return container;
	}

}
