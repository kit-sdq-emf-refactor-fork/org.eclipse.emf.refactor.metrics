package org.eclipse.emf.refactor.metrics.ocl.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class OCLDataWizardPage extends WizardPage implements Listener{

	private static final String PAGE_NAME = "org.eclipse.emf.refactor.metrics.OCLDataWizardPage";
	private static final String PAGE_TITLE = "New Metric: OCL Expression";
	private static final String PAGE_DESCRIPTION = 
			"Please enter an OCL expression specifying the calculation of the new metric.";
	
	private String oclExpression;	
	private StyledText oclExpressionTextField;	

	public OCLDataWizardPage(){
		super(PAGE_NAME);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		createTextFields(container);
		setControl(container);
		this.setPageComplete(false);

	}
	
	public String getOCLExpression(){
		return oclExpression;
	}
	
	private void createTextFields(Composite container) {
		Label label;
		GridData gridData;
		Group group;
		GridLayout layout;
		// - OCL Data -
		group = new Group(container, SWT.NONE);
		group.setText("OCL");
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
	    group.setLayoutData(gridData);
		// - Name -
		// col:1
		label = new Label(group, SWT.NONE);
		label.setText("OCL expression:  ");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING));
		// col:2
		oclExpressionTextField  = new StyledText(group, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		oclExpressionTextField.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		oclExpressionTextField.addListener(SWT.Modify, this);
		oclExpressionTextField.setText("self.");
	}

	@Override
	public void handleEvent(Event event) {
		updatePageComplete();		
	}

	private void updatePageComplete() {
		setPageComplete(! this.oclExpressionTextField.getText().isEmpty());
		saveTextFieldValues();
	}

	private void saveTextFieldValues() {
		String ocl = this.oclExpressionTextField.getText();
		ocl = ocl.replaceAll("\r", " ");
		ocl = ocl.replaceAll("\n", " ");
		oclExpression = ocl;	
	}
}
