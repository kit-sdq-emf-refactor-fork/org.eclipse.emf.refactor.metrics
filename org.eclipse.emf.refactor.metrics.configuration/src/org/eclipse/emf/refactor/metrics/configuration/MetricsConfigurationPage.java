package org.eclipse.emf.refactor.metrics.configuration;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

public class MetricsConfigurationPage extends PropertyPage {

	

	@Override
	public void createControl(Composite parent) {
		
	}

	@Override
	protected Control createContents(Composite parent) {
		return null;
	}

	@Override
	public boolean performOk() {
		return false;
	}

	@Override
	protected void performApply() {
		performOk();
	}

	@Override
	protected void performDefaults() {
		
	}
}