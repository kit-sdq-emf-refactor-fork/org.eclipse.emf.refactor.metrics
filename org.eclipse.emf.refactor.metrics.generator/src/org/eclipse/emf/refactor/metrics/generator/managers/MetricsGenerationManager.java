package org.eclipse.emf.refactor.metrics.generator.managers;

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
import org.eclipse.emf.refactor.metrics.generator.Activator;
import org.eclipse.emf.refactor.metrics.generator.core.CompositeMetricInfo;
import org.eclipse.emf.refactor.metrics.generator.core.MetricInfo;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class MetricsGenerationManager {
	
	private static MetricsGenerationManager instance;
	private static final String COMPOSITE_TEMPLATE_CLASS_NAME = "CompositeCalculateClassTemplate";
	private static final String SKELETON_TEMPLATE_CLASS_NAME = "CalculateClassTemplate";
	protected static final  String TEMPLATE_DIR = "/templates";
	private static final String SOURCE_DIR = "/src/";
	protected static final  String TEMPLATE_FILE_EXTENSION = ".javajet";
	private static final String JAVA_FILE_EXTENSION = ".java";
	protected static final String PLUGINSPATH = Platform.getInstallLocation().getURL().getPath() + "plugins/";
	protected static final String BUNDLEVERSION = "Bundle-Version";
	protected static String templateDirectory;
	protected static List<IClasspathEntry> classpathEntries;
	
	protected MetricsGenerationManager() {
		templateDirectory = setTemplateDirectory();
		classpathEntries = setClassPathEntries();
		System.out.println("GenerationManager initialized!");
	}
	
	public static MetricsGenerationManager getInstance() {
		if (instance == null) {
			instance = new MetricsGenerationManager();
		}
		return instance;
	}

	public static void createNewMetric(IProgressMonitor monitor,
			MetricInfo metricInfo, IProject targetProject) {
		System.out.println(metricInfo);
		DependenciesManager.updateDependencies(metricInfo);
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
		
	/**
	 * Generates a new metric <i>ICompositeCalculateClass</i> class.
	 * @param metricInfo 
	 * 
	 * @return progressMonitor
	 */
	private static void createCalculateClass(IProgressMonitor monitor, MetricInfo metricInfo) {
		String generatedCode = "";
		String templateName = "";
		if(metricInfo instanceof CompositeMetricInfo){
			templateName = COMPOSITE_TEMPLATE_CLASS_NAME;
		} else {
			templateName = SKELETON_TEMPLATE_CLASS_NAME;
		}
		System.out.println("Template: " + templateName);
		try {
			generatedCode = generateCode(monitor, templateName, metricInfo);
		    saveCode(monitor, generatedCode, metricInfo);
		} catch (JETException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	protected List<IClasspathEntry> setClassPathEntries() {
	    List<IClasspathEntry> cpe = new ArrayList<IClasspathEntry>();
	    Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
	    // add org.eclipse.emf.refactor.metrics.generator to class path
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
	
	@SuppressWarnings("finally")
	private static String generateCode(IProgressMonitor monitor, String template, MetricInfo metricInfo) {
		String templatePath = templateDirectory + template + TEMPLATE_FILE_EXTENSION;
		ClassLoader classLoader = metricInfo.getClass().getClassLoader();
		JETEmitter jetEmitter = new JETEmitter(templatePath, classLoader);
		jetEmitter.getClasspathEntries().addAll(classpathEntries);
		String result = "";
		IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
		try {
			result = jetEmitter.generate(subMonitor, new Object[] {metricInfo});
		} catch (JETException e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			System.out.println(result);
			return result;
		}
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

	private String setTemplateDirectory() {
		String directory = "";
		final Bundle bundle = Activator.getDefault().getBundle();
		try {
			directory = FileLocator.toFileURL(bundle.getEntry(TEMPLATE_DIR)).getFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
}
