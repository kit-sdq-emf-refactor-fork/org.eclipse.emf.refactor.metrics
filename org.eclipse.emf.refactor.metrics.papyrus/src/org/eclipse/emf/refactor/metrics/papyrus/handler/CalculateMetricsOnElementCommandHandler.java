package org.eclipse.emf.refactor.metrics.papyrus.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.papyrus.managers.PapyrusProjectManager;
import org.eclipse.emf.refactor.metrics.papyrus.managers.PapyrusSelectionManager;
import org.eclipse.emf.refactor.metrics.runtime.managers.RuntimeManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.ui.PlatformUI;

public class CalculateMetricsOnElementCommandHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) { }

	@Override
	public void dispose() { }

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Cursor oldCursor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getCursor();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(new Cursor(null,SWT.CURSOR_WAIT));
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		List<EObject> context = PapyrusSelectionManager.getESelection(selection);
		IProject project = PapyrusProjectManager.getActualProject(selection);
		if (project == null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(oldCursor);
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"EMF Quality Assurance", "Can not calculate metrics: project is " + project);
			return null;
		}
		System.out.println("context: " + context);
		RuntimeManager.getInstance();
		RuntimeManager.calculateConfiguredMetricsOnElement(project, context);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(oldCursor);
		return null;
	}

	@Override
	public boolean isEnabled() { return true; }

	@Override
	public boolean isHandled() { return true; }

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) { }

}
