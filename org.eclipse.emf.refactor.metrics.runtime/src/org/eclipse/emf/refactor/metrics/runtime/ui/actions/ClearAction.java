package org.eclipse.emf.refactor.metrics.runtime.ui.actions;

import org.eclipse.emf.refactor.metrics.runtime.ui.MetricResultsViewContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ClearAction extends Action {
	
	private TableViewer viewer;
	
	public ClearAction(TableViewer viewer) {
		super();
		this.viewer = viewer;
		this.setText("Clear Results");
		this.setToolTipText("Remove all results from the list");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		
	}
	
	public void run() {
		((MetricResultsViewContentProvider) viewer.getContentProvider()).removeAll();
	}

}
