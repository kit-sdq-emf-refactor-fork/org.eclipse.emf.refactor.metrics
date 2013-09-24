package org.eclipse.emf.refactor.metrics.core;

import java.util.LinkedList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public class MetricLoader {

	public static LinkedList<Metric> loadMetrics() {
		LinkedList<Metric> metrics = new LinkedList<Metric>();
		IConfigurationElement[] rawMetrics = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ExtensionPointTags.EXTENSION_POINT_NAME);
		for (IConfigurationElement element : rawMetrics) {
			try {
				if (element.getName().equals(ExtensionPointTags.METRIC_TAG)) {
					Metric metric = createMetric(element);
					if (null != metric) {
						metrics.add(metric);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		java.util.Collections.sort(metrics);
		return metrics;
	}

	private static Metric createMetric(IConfigurationElement rawMetric) {
		try {
			final String name = rawMetric.getAttribute(ExtensionPointTags.METRIC_NAME_TAG);
			final String id = rawMetric.getAttribute(ExtensionPointTags.METRIC_ID_TAG);
			final String description = rawMetric
					.getAttribute(ExtensionPointTags.METRIC_DESCRIPTION_TAG);
			final String metamodel = rawMetric
					.getAttribute(ExtensionPointTags.METRIC_METAMODEL_TAG);
			final String context = rawMetric.getAttribute(ExtensionPointTags.METRIC_CONTEXT_TAG);
			final IMetricCalculator calculateClass = (IMetricCalculator) rawMetric
					.createExecutableExtension(ExtensionPointTags.METRIC_CALCULATE_CLASS_TAG);
			return new Metric(name, description, metamodel, context, calculateClass, id);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

}
