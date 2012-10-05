package org.eclipse.emf.refactor.metrics.reporter;

import org.eclipse.emf.refactor.metrics.reporter.actions.ExportAction;
import org.eclipse.emf.refactor.metrics.runtime.managers.RuntimeManager;
import org.eclipse.ui.IStartup;

public class ReporterStartup implements IStartup {

	@Override
	public void earlyStartup() { 
		RuntimeManager.getInstance(new ExportAction());
		System.out.println("Started");
	}

}
