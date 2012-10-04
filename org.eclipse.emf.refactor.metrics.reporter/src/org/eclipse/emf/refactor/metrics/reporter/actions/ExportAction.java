package org.eclipse.emf.refactor.metrics.reporter.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ExportAction extends Action {

	public ExportAction() {
		super();
		this.setText("to do");
		this.setToolTipText("to do");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_PRINT_EDIT));
	}

	public void run() {
		System.out.println("IN: ExportAction:: run() => TO DO ...");
	}

}
