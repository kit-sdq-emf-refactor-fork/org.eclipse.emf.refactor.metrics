package org.eclipse.emf.refactor.metrics.runtime.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;


public class MetricResultsViewContentProvider implements IStructuredContentProvider{
	
	
	public static final String DESCRIPTION = "description";
	public static final String METRIC_NAME = "metric";
	public static final String CONTEXT = "context";
	public static final String VALUE = "value";
	public static final String TIME = "time";
	private String lastSortColumn = "";
	private boolean ascending = true;
	
	private List<Result> content;
	private TableViewer viewer;
	
	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer)viewer;
		content = (List<Result>)newInput;
	}
	
	public Object[] getElements(Object parent) {
		List<Result> result = content;
		return result.toArray();
	}
	
	public void removeAll(){
		viewer.remove(content.toArray());
		content.clear();
	}
	
	public void dispose() {}
	
	public void sortContent(String columnName){
		if(columnName.equals(lastSortColumn))
			ascending = !ascending;
		else
			ascending = true;
		Result[] resultArray = new Result[0];
		resultArray = content.toArray(resultArray);
		for(int i = 0; i < resultArray.length; i++){
			for(int j = 0; j < resultArray.length; j++){
				if(ascending && compare(resultArray[i], resultArray[j], columnName) < 0){
					swap(resultArray, i, j);
				}else if(!ascending && compare(resultArray[i], resultArray[j], columnName) > 0)
					swap(resultArray, i, j);
			}
		}
		LinkedList<Result> newInput = new LinkedList<Result>();
		for(Result result : resultArray){
			newInput.add(result);
		}
		viewer.setInput(newInput);
		lastSortColumn = columnName;
	}
	
	private int compare(Result result1, Result result2, String columnName){
		if(columnName.equals(METRIC_NAME)){
			return result1.getMetric().getName().compareTo(result2.getMetric().getName());
		}else if(columnName.equals(CONTEXT)){
			return MetricResultsViewLabelProvider.getEObjectLabel(result1.getContext().get(0))
					.compareTo(MetricResultsViewLabelProvider.getEObjectLabel(result2.getContext().get(0)));
		}else if(columnName.equals(TIME)){
			return result1.getTimeStamp().compareTo(result2.getTimeStamp());
		}else if(columnName.equals(DESCRIPTION)){
			return result1.getMetric().getDescription().compareTo(result2.getMetric().getDescription());
		}else if(columnName.equals(VALUE)){
			Double value1 = result1.getResultValue();
			Double value2 = result2.getResultValue();
			return value1.compareTo(value2);
		}
		return 0;
	}
	
	private void swap(Result[] result, int i, int j){
		Result temp = result[i];
		result[i] = result[j];
		result[j] = temp;
	}
	
}
