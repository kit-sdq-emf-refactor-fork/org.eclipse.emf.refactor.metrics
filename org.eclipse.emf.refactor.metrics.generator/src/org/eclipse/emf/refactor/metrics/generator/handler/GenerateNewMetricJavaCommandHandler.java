package org.eclipse.emf.refactor.metrics.generator.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.refactor.metrics.generator.ui.NewMetricWizardJava;
import org.eclipse.emf.refactor.metrics.managers.SelectionManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class GenerateNewMetricJavaCommandHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) { }

	@Override
	public void dispose() {	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = win.getSelectionService().getSelection();
		List<EObject> context = SelectionManager.getESelection(selection);
		EObject selectedEObject = context.get(0);
		String metamodel = ((EPackage) selectedEObject.eClass().eContainer()).getNsURI();		
		String contextType = selectedEObject.eClass().getInstanceClass().getSimpleName();		
		NewMetricWizardJava wizard = new NewMetricWizardJava(metamodel, contextType);
		wizard.init(win.getWorkbench(), (IStructuredSelection) selection);
		WizardDialog dialog = new WizardDialog(win.getShell(), wizard);
		return dialog.open();
	}

	@Override
	public boolean isEnabled() { return true; }

	@Override
	public boolean isHandled() { return true; }

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) { }

}
