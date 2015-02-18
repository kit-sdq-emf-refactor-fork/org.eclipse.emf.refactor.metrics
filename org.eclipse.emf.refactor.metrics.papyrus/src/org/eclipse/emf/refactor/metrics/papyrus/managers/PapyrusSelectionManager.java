package org.eclipse.emf.refactor.metrics.papyrus.managers;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.facet.infra.browser.uicore.internal.model.ModelElementItem;
import org.eclipse.emf.refactor.metrics.managers.SelectionManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;

//@SuppressWarnings("restriction")
public class PapyrusSelectionManager extends SelectionManager {

	public static List<EObject> getESelection(ISelection selection) {
		if (selection == null)
			return null;
		List<EObject> r = SelectionManager.getESelection(selection);
		for (Object o : getSelection(selection)) {
//			if (o instanceof ModelElementItem) {
//				System.out.println("instanceof ModelElementItem");
//				ModelElementItem mei = (ModelElementItem) o;
//	    		System.out.println("element: " + mei.getEObject());
//	    		r.add(mei.getEObject());
//			} else {
				if (o instanceof IGraphicalEditPart) {
		    		System.out.println("instanceof IGraphicalEditPart");
		    		IGraphicalEditPart gep = (IGraphicalEditPart) o;
		    		System.out.println("element: " + gep.resolveSemanticElement());
		    		r.add(gep.resolveSemanticElement());
		    	} else {
		    		EObject eObject = EMFHelper.getEObject(o);
		    		if (eObject != null) {
		    				r.add(eObject);
		    		}
		    	}
//			}
		}
		return r;
	}

}
