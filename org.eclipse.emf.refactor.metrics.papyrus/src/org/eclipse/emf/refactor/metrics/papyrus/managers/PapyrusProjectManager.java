package org.eclipse.emf.refactor.metrics.papyrus.managers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.managers.ProjectManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class PapyrusProjectManager extends ProjectManager {
	
	@SuppressWarnings("finally")
	public static IProject getActualProject(ISelection selection) {
		IProject actualProject = ProjectManager.getActualProject();
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		try {
			IEditorPart editorPart = window.getActivePage().getActiveEditor();
			if (editorPart != null) {
				IEditorInput input = editorPart.getEditorInput();
				if (input instanceof IFileEditorInput) {
					IFileEditorInput fileInput = (IFileEditorInput) input;
					actualProject = fileInput.getFile().getProject();
				}
				if (input instanceof DiagramEditorInput) {
					actualProject = getProjectFromISelection(selection);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return actualProject;
		}
	}
	
	private static IProject getProjectFromISelection(ISelection selection) {
		IProject project = null;
		Object o = PapyrusSelectionManager.getSelection(selection)[0];
		if (o instanceof IGraphicalEditPart) {
		    System.out.println("instanceof IGraphicalEditPart");
		    IGraphicalEditPart gep = (IGraphicalEditPart) o;
		    EObject element = gep.resolveSemanticElement();
		    System.out.println("element: " + element);
		    if (element != null) {
			    URI uri = gep.resolveSemanticElement().eResource().getURI();
			    System.out.println("URI: " + uri);
			    String projectName = uri.segmentsList().get(1).toString();
			    project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		    }
		} 
		return project;
	}
}
