package org.eclipse.emf.refactor.metrics.configuration.ui;

import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.refactor.metrics.configuration.core.SortedMetricsList;
import org.eclipse.emf.refactor.metrics.configuration.managers.ConfigurationManager;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.managers.MetricManager;
import org.eclipse.emf.refactor.metrics.managers.ProjectManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class MetricsConfigurationPage extends PropertyPage {

	private static final String CONTEXT_TABLE_DATA_KEY = "context";
	private static final String METAMODEL_TABLE_DATA_KEY = "metamodel";
	private IProject project;
	private TableItemsMapping mapping;
	private SortedMetricsList metricList;
	private Table[] metricsTables;

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.getDefaultsButton().setText("(De-)Select All");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 20;
		layout.horizontalSpacing = 10;
		layout.marginHeight = 20;
		layout.marginWidth = 10;
		layout.numColumns = 1;
		panel.setLayout(layout);
		metricList = new SortedMetricsList(MetricManager.getAllMetrics());
		mapping = new TableItemsMapping(MetricManager.getAllMetrics());
		createHeader(panel);
		createMetricTables(panel);
		project = ProjectManager.getActualProjectForPropertyPage(this);
		fillTables();
		loadSelection();
		return panel;
	}

	@Override
	public boolean performOk() {
		saveSelection();
		ConfigurationManager.getInstance();
		ConfigurationManager.saveConfiguration(project);
		return true;
	}

	@Override
	protected void performApply() {
		performOk();
	}

	@Override
	protected void performDefaults() {
		boolean allSelected = true;
		for (int index = 0; index < mapping.getSize(); index++) {
			if(! mapping.getTableItem(index).getChecked()){
				allSelected = false;
				break;
			}
		}
		for (int index = 0; index < mapping.getSize(); index++) {
			mapping.getTableItem(index).setChecked(!allSelected);
		}
	}

	private void createHeader(Composite panel) {
		Text text = new Text(panel, SWT.LEFT);
		text.setBackground(panel.getBackground());
		text.setText("Please select EMF metrics from the list below to be supported by the project.");
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		text.setLayoutData(gridData);
	}

	private void createMetricTables(Composite panel) {
		TabFolder tabs = new TabFolder(panel, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalAlignment = GridData.FILL;
		tabs.setLayoutData(gridData);
		metricsTables = new Table[metricList.getContextCount()];
		int tableArrayPointer = 0;
		for (int index = 0; index < metricList.getMetricsList().size(); index++) {
			TabItem tabItem = new TabItem(tabs, SWT.LEFT);
			tabItem.setText(""+ metricList.getMetricsList().get(index).get(0).get(0).getMetamodel());
			Composite composite = new Composite(tabs, SWT.NULL);
			tabItem.setControl(composite);
			GridLayout layout = new GridLayout();
			composite.setLayout(layout);
			LinkedList<LinkedList<Metric>> metamodelList = metricList.getMetamodelList(tabItem.getText());
			//for each context list create a table in the tab
			for(LinkedList<Metric> list : metamodelList){
				Label contextLabel = new Label(composite, SWT.LEFT);
				contextLabel.setText(list.get(0).getContext());
				//======
				metricsTables[tableArrayPointer] = new Table(composite, SWT.CHECK | SWT.SINGLE
						| SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL);
				metricsTables[tableArrayPointer].setHeaderVisible(true);
				gridData = new GridData(GridData.FILL_BOTH);
				metricsTables[tableArrayPointer].setLayoutData(gridData);
				metricsTables[tableArrayPointer].setData(METAMODEL_TABLE_DATA_KEY, tabItem.getText());
				metricsTables[tableArrayPointer].setData(CONTEXT_TABLE_DATA_KEY, contextLabel.getText());
				TableColumn col;
				col = new TableColumn(metricsTables[tableArrayPointer], SWT.CENTER
						| SWT.READ_ONLY);
				col.setText("Selected");
				col.setWidth(60);
				col = new TableColumn(metricsTables[tableArrayPointer], SWT.LEFT
						| SWT.READ_ONLY);
				col.setText("Name");
				col.setWidth(150);
				col = new TableColumn(metricsTables[tableArrayPointer], SWT.LEFT
						| SWT.READ_ONLY);
				col.setText("Description");
				col.setWidth(250);
//				col = new TableColumn(metricsTables[tableArrayPointer], SWT.LEFT
//						| SWT.READ_ONLY);
//				col.setText("Context");
//				col.setWidth(100);
				final TableEditor editor = new TableEditor(metricsTables[tableArrayPointer]);
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				tableArrayPointer++;
				//======
			}
		}
	}

	private void fillTables() {
		for(LinkedList<LinkedList<Metric>> metamodelList : metricList.getMetricsList()){
			for(LinkedList<Metric> contextList : metamodelList){
				for(Metric metric : contextList){
					TableItem item = new TableItem(metricsTables[getMetricTableArrayIndex(metric)], SWT.NONE);
					item.setText(1, metric.getName());
					item.setText(2, metric.getDescription());
//					item.setText(3, metric.getContext());
					mapping.setItemForMetric(metric, item);
				}
			}
		}
	}

	private void loadSelection() {	
		ConfigurationManager.getInstance();
		LinkedList<Metric> selectedMetrics = ConfigurationManager.getSelectedMetrics(project);
		if(selectedMetrics==null || selectedMetrics.isEmpty())
			for(int i=0; i < mapping.getSize(); i++)
				mapping.getTableItem(i).setChecked(true);
		for (Metric metric : selectedMetrics)
			mapping.getTableItem(metric).setChecked(true);
	}

	private void saveSelection() {
		boolean[] selection = new boolean[mapping.getSize()];
		for (int index = 0; index < mapping.getSize(); index++) {
			selection[index] = mapping.getTableItem(index).getChecked();
		}
		ConfigurationManager.getInstance();
		ConfigurationManager.setConfiguration(project, selection);
	}
	
	private int getMetricTableArrayIndex(Metric metric){
		for(int i = 0; i<metricsTables.length; i++){
			Table table = metricsTables[i];
			String metamodel = (String) table.getData(METAMODEL_TABLE_DATA_KEY);
			String context = (String)table.getData(CONTEXT_TABLE_DATA_KEY);
			if(metamodel.equals(metric.getMetamodel()) && context.equals(metric.getContext()))
				return i;
		}
		return -1;
	}
}