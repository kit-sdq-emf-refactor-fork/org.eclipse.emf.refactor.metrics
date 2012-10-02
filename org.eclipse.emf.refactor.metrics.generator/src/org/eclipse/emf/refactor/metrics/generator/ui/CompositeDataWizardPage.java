package org.eclipse.emf.refactor.metrics.generator.ui;

import java.util.LinkedList;

import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.managers.MetricManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CompositeDataWizardPage extends WizardPage {

	private static final String PAGE_NAME = "org.eclipse.emf.refactor.metrics.CompositeDataWizardPage";
	private static final String TITLE = "New Metric: Composite Data";
	private static final String DESCRIPTION = "Please specify two input metrics and a join operation " +
			"for the new metric.";
	private TableViewer viewer;
	private Table firstMetricTable, secondMetricTable;
	private TableItemsMapping firstTableMapping, secondTableMapping;
	private Combo operationCombo;
	private Metric firstMetric = null, secondMetric = null;
	private String operation;
			
	public Metric getFirstMetric() {
		return firstMetric;
	}

	public Metric getSecondMetric() {
		return secondMetric;
	}

	public CompositeDataWizardPage() {
		super(PAGE_NAME);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
	}

	protected String getOperationName(){
		return operation;
	}
	
	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	@Override
	public void createControl(Composite parent) {
		MetricManager.getInstance();
		firstTableMapping = new TableItemsMapping(MetricManager.getAllMetrics());
		secondTableMapping = new TableItemsMapping(MetricManager.getAllMetrics());
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		setControl(container);
		createContent(container);
		fillTables();
		fillOperationNames();
		this.setPageComplete(false);
	}

	private void createContent(Composite container) {
		TableColumn col;
		GridData gridData;
		Group group;
		GridLayout layout;
		// - First Metric -
		group = new Group(container, SWT.NONE);
		group.setText("First metric");
	    layout = new GridLayout();
	    layout.numColumns = 1;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_BOTH);
	    gridData.horizontalSpan = 2;
	    group.setLayoutData(gridData);
		firstMetricTable = new Table(group, SWT.CHECK | SWT.SINGLE
				 | SWT.BORDER | SWT.V_SCROLL);
		firstMetricTable.setHeaderVisible(true);
		firstMetricTable.addSelectionListener(new MetricTableSelectionListener(
				firstMetricTable));
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    gridData.widthHint = 400;
		firstMetricTable.setLayoutData(gridData);
		col = new TableColumn(firstMetricTable, SWT.CENTER | SWT.READ_ONLY);
		col.setText("Selected");
		col.setWidth(60);
		col = new TableColumn(firstMetricTable, SWT.LEFT | SWT.READ_ONLY);
		col.setText("Name");
		col.setWidth(100);
		col = new TableColumn(firstMetricTable, SWT.LEFT | SWT.READ_ONLY);
		col.setText("Description");
		col.setWidth(200);
		
		// - Operation -
		group = new Group(container, SWT.NONE);
		group.setText("Operation");
	    layout = new GridLayout();
	    layout.numColumns = 1;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_BOTH);
	    gridData.horizontalSpan = 2;
	    group.setLayoutData(gridData);
	    operationCombo = new Combo(group, SWT.READ_ONLY);
	    operationCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    operationCombo.setText("Select operation");
	    operationCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setOperationName(((Combo)e.getSource()).getText());
				updatePageComplete();
			}
		});
	    
	 // - Second Metric -
	 		group = new Group(container, SWT.NONE);
	 		group.setText("Second metric");
	 	    layout = new GridLayout();
	 	    layout.numColumns = 1;
	 	    group.setLayout(layout);
	 	    gridData = new GridData(GridData.FILL_BOTH);
	 	    gridData.horizontalSpan = 2;
	 	    group.setLayoutData(gridData);
	 		viewer = new TableViewer(group, SWT.CHECK  | SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL);
	 	    secondMetricTable = viewer.getTable();
	 		secondMetricTable.setHeaderVisible(true);
	 		secondMetricTable.addSelectionListener(new MetricTableSelectionListener(
	 						secondMetricTable));
	 		gridData = new GridData(GridData.FILL_HORIZONTAL);
	 	    gridData.heightHint = 100;
	 	    gridData.widthHint = 400;
	 		secondMetricTable.setLayoutData(gridData);
	 		col = new TableColumn(secondMetricTable, SWT.CENTER | SWT.READ_ONLY);
	 		col.setText("Selected");
	 		col.setWidth(60);
	 		col = new TableColumn(secondMetricTable, SWT.LEFT | SWT.READ_ONLY);
	 		col.setText("Name");
	 		col.setWidth(100);
	 		col = new TableColumn(secondMetricTable, SWT.LEFT | SWT.READ_ONLY);
	 		col.setText("Description");
	 		col.setWidth(200);
	}
	
	protected void fillTables() {
		String metamodel = ((NewMetricWizardComposite)getWizard()).getMetamodel();
		String context = ((NewMetricWizardComposite)getWizard()).getContext() ;
		System.out.println("filling tables for metamodel:"+metamodel+" context:"+context);
		firstMetricTable.removeAll();
		secondMetricTable.removeAll();
		LinkedList<Metric> metrics = MetricManager.getFilteredMetrics(metamodel, context);
		System.out.println("found:"+metrics.size()+" metrics"); 
		TableItem item;
		for (Metric metric : metrics) {
			item = new TableItem(firstMetricTable, SWT.NONE);
			item.setText(1, metric.getName());
			item.setText(2, metric.getDescription());
			item.setText(3, metric.getContext());
			firstTableMapping.setItemForMetric(metric, item);
			item = new TableItem(secondMetricTable, SWT.NONE);
			item.setText(1, metric.getName());
			item.setText(2, metric.getDescription());
			item.setText(3, metric.getContext());
			secondTableMapping.setItemForMetric(metric, item);
		}
	}

	private void fillOperationNames(){
		String[] operationNames = MetricManager.getOperationNames();
		for(String name : operationNames)
			operationCombo.add(name);
	}
	
	/**
	 * Wird jedes mal ausgeführ wenn sich der Inhalt eines Textfeldes im Wizard
	 * verändert. Überprüft die inhalte der Textfelder und erzeugt
	 * entschprechende Meldungen im Wizardfenster.
	 */
	private void updatePageComplete() {
		this.firstMetric = getSelectedMetric(this.firstTableMapping);
		this.secondMetric = getSelectedMetric(this.secondTableMapping);
		if (this.firstMetric == null) {
			setMessage("First Metric is not selected", ERROR);
			this.setPageComplete(false);
			getWizard().getContainer().updateButtons();
		} else if (this.secondMetric == null) {
			setMessage("Second Metric is not selected", ERROR);
			this.setPageComplete(false);
			getWizard().getContainer().updateButtons();
		} else if (!(this.operationCombo.getSelectionIndex() >= 0)) {
			setMessage("Join operation is not selected", ERROR);
			this.setPageComplete(false);
			getWizard().getContainer().updateButtons();
		} else {
			setMessage("");
			this.setPageComplete(true);
			getWizard().getContainer().updateButtons();
		}
	}

	private void setOperationName(String name){
		operation = name;
	}
	
	private Metric getSelectedMetric(TableItemsMapping mapping) {
		for (int i = 0; i < mapping.getSize(); i++) {
			if(mapping.getTableItem(i)!=null)
				if (mapping.getTableItem(i).getChecked()) {
					return mapping.getMetric(i);
				}
		}
		return null;
	}


	// -------------------------------------------------------------------------------
	private class MetricTableSelectionListener implements SelectionListener {

		Table table;

		MetricTableSelectionListener(Table table) {
			this.table = table;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			for (TableItem item : table.getItems()) {
				item.setChecked(false);
			}
			((TableItem) e.item).setChecked(true);
			updatePageComplete();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {}

	}

}
