package org.eclipse.emf.refactor.metrics.managers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectManager {

	public static IProject getActualProjectForPropertyPage(PropertyPage page) {
		IProject project;
		if (page.getElement() instanceof IJavaProject) {
			project = ((IJavaProject) page.getElement()).getProject();
		} else {
			project = ((IProject) page.getElement());
		}
		return project;
	}

	@SuppressWarnings("finally")
	public static IProject getActualProject() {
		IProject actualProject = null;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return actualProject;
		}
	}
}
