package org.eclipse.emf.refactor.metrics.papyrus;

import org.eclipse.emf.refactor.metrics.papyrus.ui.PapyrusHighlighting;
import org.eclipse.emf.refactor.metrics.runtime.managers.RuntimeManager;
import org.eclipse.ui.IStartup;

public class PapyrusStartup implements IStartup {

	@Override
	public void earlyStartup() {
		RuntimeManager.getInstance(new PapyrusHighlighting());
		System.out.println("Started");
	}

}
