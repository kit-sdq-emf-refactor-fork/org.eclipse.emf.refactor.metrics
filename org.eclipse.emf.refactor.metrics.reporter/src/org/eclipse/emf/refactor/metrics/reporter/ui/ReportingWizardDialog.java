package org.eclipse.emf.refactor.metrics.reporter.ui;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class ReportingWizardDialog extends WizardDialog {

	public ReportingWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
//		setMinimumPageSize(300, 300);
	}

}
