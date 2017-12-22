package org.eclipse.emf.refactor.metrics.xtext;

import org.eclipse.emf.refactor.metrics.runtime.managers.RuntimeManager;
import org.eclipse.emf.refactor.metrics.xtext.ui.XtextHighlighting;
import org.eclipse.ui.IStartup;

public class XtextStartup implements IStartup {

	@Override
	public void earlyStartup() {
		RuntimeManager.getInstance(new XtextHighlighting());
		System.out.println("Started");
	}

}
