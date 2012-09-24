package org.eclipse.emf.refactor.metrics.configuration.ui;

import java.util.LinkedList;

import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.swt.widgets.TableItem;

public class TableItemsMapping {

	private TableItemMapping[] tableItems;

	public TableItemsMapping(LinkedList<Metric> inputList) {
		LinkedList<TableItemMapping> tableItemsList = new LinkedList<TableItemMapping>();
		for (Metric metric : inputList) {
			tableItemsList.add(new TableItemMapping(metric));
		}
		tableItems = new TableItemMapping[tableItemsList.size()];
		for (int i = 0; i < tableItemsList.size(); i++) {
			tableItems[i] = tableItemsList.get(i);
		}
	}

	public int getSize() {
		return tableItems.length;
	}

	public void setItemForMetric(Metric metric, TableItem item) {
		int index = getTableItemIndex(metric);
		setItem(index, item);
	}

	public Metric getMetric(int index){
		return tableItems[index].getMetric();
	}
	
	public TableItem getTableItem(int index) {
		return tableItems[index].getItem();
	}

	public TableItem getTableItem(Metric metric) {
		return tableItems[getTableItemIndex(metric)].getItem();
	}

	private void setItem(int index, TableItem item) {
		tableItems[index].setItem(item);
	}

	private int getTableItemIndex(Metric metric) {
		for (int i = 0; i < tableItems.length; i++) {
			if (tableItems[i].getMetric().equals(metric))
				return i;
		}
		return -1;
	}

	private class TableItemMapping {
		private TableItem item;
		private Metric metric;

		private TableItemMapping(Metric metric) {
			this.metric = metric;
			this.item = null;
		}

		private void setItem(TableItem item) {
			this.item = item;
		}

		private TableItem getItem() {
			return item;
		}

		private Metric getMetric() {
			return metric;
		}

	}
}
