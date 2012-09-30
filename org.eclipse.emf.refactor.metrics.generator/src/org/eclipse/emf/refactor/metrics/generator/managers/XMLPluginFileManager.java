package org.eclipse.emf.refactor.metrics.generator.managers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.refactor.metrics.core.ExtensionPointTags;
import org.eclipse.emf.refactor.metrics.managers.XMLManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLPluginFileManager extends XMLManager {

	public static final String PLUGIN_FILE = "/plugin.xml";
	public static final String PLUGIN_TAG = "plugin";
	public static final String EXTENSION_TAG = "extension";
	public static final String POINT_ATTR_TAG = "point";
	
	public static void createMetricEntry(String project, String name,
			String description, String metamodel, String context, 
			String id, String calculateClass) {
		final DocumentBuilder builder = createDocumentBuilder();
		final String path = project + PLUGIN_FILE;
		Document doc = null;
		final File file = new File(path);
		if (!file.exists())
			createPluginFile(project);
		if (builder != null) {
			Element root;
			try {
				doc = builder.parse(path);
			} catch (final SAXException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			if (doc != null) {
				root = doc.getDocumentElement();
				Node extensionPoint = root.getElementsByTagName(EXTENSION_TAG).item(0);
				Node newEntry = createMetricElement(doc, name, description,	metamodel, context, id, calculateClass);
				extensionPoint.appendChild(newEntry);
			}
		}
		final Transformer transformer = createTransformer();
		final DOMSource source = new DOMSource(doc);
		final StreamResult result = new StreamResult(path);
		try {
			transformer.transform(source, result);
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
	}

	private static void createPluginFile(String project) {
		final DocumentBuilder builder = createDocumentBuilder();
		final String path = project + PLUGIN_FILE;
		Document doc = null;
		Element root;
		if (builder != null) {
			doc = builder.newDocument();
			root = doc.createElement(PLUGIN_TAG );
			doc.appendChild(root);
			final Element extension = doc.createElement(ExtensionPointTags.EXTENSION_POINT_TAG);
			extension.setAttribute(POINT_ATTR_TAG, ExtensionPointTags.EXTENSION_POINT_NAME);
			root.appendChild(extension);
		}
		final Transformer transformer = createTransformer();
		final DOMSource source = new DOMSource(doc);
		final StreamResult result = new StreamResult(path);
		try {
			transformer.transform(source, result);
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
	}

	private static Element createMetricElement(Document doc, String name,
			String id, String description, String metamodel, String context, String calculateClass) {
		final Element metric = doc.createElement(ExtensionPointTags.METRIC_TAG);
		metric.setAttribute(ExtensionPointTags.METRIC_NAME_TAG, name);
		metric.setAttribute(ExtensionPointTags.METRIC_ID_TAG, id);
		metric.setAttribute(ExtensionPointTags.METRIC_DESCRIPTION_TAG, description);
		metric.setAttribute(ExtensionPointTags.METRIC_METAMODEL_TAG, metamodel);
		metric.setAttribute(ExtensionPointTags.METRIC_CONTEXT_TAG, context);
		metric.setAttribute(ExtensionPointTags.METRIC_CALCULATE_CLASS_TAG, calculateClass);
		return metric;
	}
}
