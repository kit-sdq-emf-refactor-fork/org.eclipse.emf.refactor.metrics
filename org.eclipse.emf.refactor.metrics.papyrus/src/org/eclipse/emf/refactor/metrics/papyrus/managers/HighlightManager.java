package org.eclipse.emf.refactor.metrics.papyrus.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy.DecoratorTarget;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;


public class HighlightManager {
	
	private static HighlightManager instance = null;	
	private List<EObject> selected; 
	private List<IDecorator> decorators = null;
	private Map<EObject, IDecoratorTarget> decoratorTargets = null;
	
	private HighlightManager() {
		selected = new ArrayList<EObject>();
		decoratorTargets = new HashMap<EObject, IDecoratorTarget>();
		decorators = new ArrayList<IDecorator>();
	}
	
	public static HighlightManager getInstance() {
		if(instance == null) {
			instance = new HighlightManager();
		}
		return instance;
	}
	
	public void highlight(Object selection) {
		// clear former selected eObjects
		selected.clear();
		// set selected eObjects from selection
		if (selection instanceof StructuredSelection) {		
			StructuredSelection ss = (StructuredSelection) selection;
			@SuppressWarnings("unchecked")
			Iterator<Object> it = ss.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Result) {
					selected.addAll(((Result) o).getContext());
				}
			}
		}
		// refresh each corresponding decorator
		for (IDecorator decorator : decorators) {
			decorator.refresh();
		}
	}

	public void registerDecorator(IDecorator decorator, DecoratorTarget decoratorTarget) {
		View view = (View) decoratorTarget.getAdapter(View.class);
		if(decoratorTargets.containsKey(view.getElement())){
			View parent = view;
			boolean topMost = false;
			if(!(parent instanceof Diagram)){
				while(!((parent = (View) parent.eContainer()) instanceof Diagram)){
					if(parent == decoratorTargets.get(view.getElement()).getAdapter(View.class)){
						topMost = true;
					}
				}
				if(!topMost){
					decoratorTargets.put(view.getElement(), decoratorTarget);
				}
			}
		} else {
			decoratorTargets.put(view.getElement(), decoratorTarget);
		}

		decorators.add(decorator);
	}

	public void unregisterDecorator(IDecorator decorator) {
		decorators.remove(decorator);
	}

	public IDecoratorTarget getPrefferedDecoratorTarget(EObject element) {
		return decoratorTargets.get(element);
	}

	public List<EObject> getSelected() {
		return selected;
	}
		
}
