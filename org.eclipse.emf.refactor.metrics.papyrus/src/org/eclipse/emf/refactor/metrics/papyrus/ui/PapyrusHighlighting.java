package org.eclipse.emf.refactor.metrics.papyrus.ui;

import org.eclipse.emf.refactor.metrics.interfaces.IHighlighting;
import org.eclipse.emf.refactor.metrics.papyrus.managers.HighlightManager;

public class PapyrusHighlighting implements IHighlighting {

	@Override
	public void highlight(Object selection) {
		System.out.println("==>> highlight selection: " + selection);
		HighlightManager.getInstance().highlight(selection);
	}

}
