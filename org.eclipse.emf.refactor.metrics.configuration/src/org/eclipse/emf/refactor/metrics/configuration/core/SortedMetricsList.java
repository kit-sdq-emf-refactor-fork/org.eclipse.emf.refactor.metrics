package org.eclipse.emf.refactor.metrics.configuration.core;

import java.util.LinkedList;

import org.eclipse.emf.refactor.metrics.core.Metric;

public class SortedMetricsList {

	private LinkedList<LinkedList<LinkedList<Metric>>> metricsList;

	public SortedMetricsList(LinkedList<Metric> rawMetricsList) {
		metricsList = new LinkedList<LinkedList<LinkedList<Metric>>>();
		for (int position = 0; position < rawMetricsList.size(); position++) {
			addMetricToList(rawMetricsList.get(position));
		}
	}

	protected int getSize(){
		return metricsList.size();
	}
	
	protected LinkedList<LinkedList<Metric>> get(int index){
		return metricsList.get(index);
	}
	
	public LinkedList<LinkedList<LinkedList<Metric>>> getMetricsList() {
		return metricsList;
	}

	private void addMetricToList(Metric metric) {
		final String metamodel = metric.getMetamodel();
		final String context = metric.getContext();
		LinkedList<Metric> contextList = getMetricList(metamodel, context);
		if(contextList == null){
			LinkedList<LinkedList<Metric>> metamodelList = getMetamodelList(metamodel);
			if(metamodelList == null){
				metamodelList = new LinkedList<LinkedList<Metric>>();
				metricsList.add(metamodelList);
			}
			contextList = new LinkedList<Metric>();
			metamodelList.add(contextList);
		}
		contextList.add(metric);
//		LinkedList<LinkedList<Metric>> list = getMetamodelList(metamodel);
//		if (list == null) {
//			list = new LinkedList<LinkedList<Metric>>();
//			metricsList.add(list);
//		}
//		list.add(metric);
	}

	public LinkedList<LinkedList<Metric>> getMetamodelList(String metamodel) {
		for (LinkedList<LinkedList<Metric>> list : metricsList) {
			try{
				if (list.get(0).get(0).getMetamodel().equals(metamodel))
					return list;
			}catch(NullPointerException ex){
				return null;
			}

		}
		return null;
	}
	
	private LinkedList<Metric> getMetricList(String metamodel, String context){
		try{
			LinkedList<LinkedList<Metric>> metamodelList = getMetamodelList(metamodel);
			for(LinkedList<Metric> innerList : metamodelList){
				if(innerList.get(0).getContext().equals(context))
					return innerList;
			}
		}catch(NullPointerException ex){
			return null;
		}
		return null;
	}
	
	public int getContextCount(){
		int contextCount = 0;
		for(LinkedList<LinkedList<Metric>> metamodelList : metricsList) {
			contextCount += metamodelList.size();
//			for(LinkedList<Metric> contextList : metamodelList){
//				contextCount++;
//			}
		}
		return contextCount;
	}
}