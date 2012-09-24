package org.eclipse.emf.refactor.metrics.managers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class XMLManager {
	
	protected static final String METRIC_TAG = "metric";
	protected static final String METRIC_ID_TAG = "metricId";
	protected  static final String METRIC_NAME_TAG = "metricName";	
	
	protected static Transformer createTransformer(){
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			return transformer;
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
			return null;
		}		
	}
		
	protected  static DocumentBuilder createDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
