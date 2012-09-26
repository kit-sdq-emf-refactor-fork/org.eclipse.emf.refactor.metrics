package org.eclipse.emf.refactor.metrics.generator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class DependenciesManager {

	private final static String ECORE = "org.eclipse.emf.ecore";
	private final static String REFACTOR_METRICS = "org.eclipse.emf.refactor.metrics";
	private final static String REQ_BUNDLE = "Require-Bundle";
	private final static String MANIFEST_FILE = "/META-INF/MANIFEST.MF";
	
	public static void updateDependencies(MetricInfo info) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(info.getProjectName());
		try {
			URL newUrl = new URL(project.getLocationURI().toURL().toString()
					+ MANIFEST_FILE);
			InputStream is = newUrl.openStream();
			Manifest mf = new Manifest(is);
			Attributes att = mf.getMainAttributes();
			String value = att.getValue(REQ_BUNDLE);
			if (!value.contains(ECORE))
				value = value + "," + ECORE;
			if (!value.contains(REFACTOR_METRICS))
				value = value + "," + REFACTOR_METRICS;
			if (! value.contains(info.getJar()))
				value = value + "," +  info.getJar();
			att.putValue(REQ_BUNDLE, value);
			FileOutputStream out = new FileOutputStream(newUrl.getFile());
			mf.write(out);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
