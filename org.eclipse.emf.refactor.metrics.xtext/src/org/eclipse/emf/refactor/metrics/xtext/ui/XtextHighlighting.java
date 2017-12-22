package org.eclipse.emf.refactor.metrics.xtext.ui;

import org.eclipse.emf.refactor.metrics.interfaces.IHighlighting;
import org.eclipse.emf.refactor.metrics.xtext.managers.XtextHighlightManager;

public class XtextHighlighting implements IHighlighting {

	@Override
	public void highlight(Object selection) {
		XtextHighlightManager.getInstance().highlight(selection);
	}

}
