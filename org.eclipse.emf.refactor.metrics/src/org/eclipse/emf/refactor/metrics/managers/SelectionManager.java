package org.eclipse.emf.refactor.metrics.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class SelectionManager {

	public static EObject getSelectedEObject(ISelection selection) {
		if (selection != null)
			if(!selection.isEmpty())
				if (getESelection(selection)!=null)
					return getESelection(selection).get(0);
		return null;
	}

	public static List<EObject> getESelection(ISelection selection) {
		System.out.println("selection: " + selection);
		if (selection == null)
			return null;
		List<EObject> r = new ArrayList<EObject>();
		for (Object o : getSelection(selection)) {
			if (o instanceof org.eclipse.emf.ecore.EObject) {
				r.add((EObject) o);
			}
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	public static Object[] getSelection(ISelection selection) {
		System.out.println("selection instanceof StructuredSelection: " + 
									(selection instanceof StructuredSelection));
		if (null != selection) {
			if (selection instanceof StructuredSelection) {
				StructuredSelection ss = (StructuredSelection) selection;
				if (null == ss.getFirstElement())
					return ss.toArray();
				List<Object> list = new ArrayList<Object>();
				final Iterator<Object> i = ss.iterator();
				while (i.hasNext()) {
					final Object selectedEObject = i.next();
					list.add(selectedEObject);
				}
				return list.toArray();
			}
		}
		return new Object[0];
	}

}
