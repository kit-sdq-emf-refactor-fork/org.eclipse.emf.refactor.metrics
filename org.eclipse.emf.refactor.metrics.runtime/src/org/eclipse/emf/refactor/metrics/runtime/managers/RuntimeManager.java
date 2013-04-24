package org.eclipse.emf.refactor.metrics.runtime.managers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.configuration.core.Configuration;
import org.eclipse.emf.refactor.metrics.configuration.managers.ConfigurationManager;
import org.eclipse.emf.refactor.metrics.interfaces.IHighlighting;
import org.eclipse.emf.refactor.metrics.runtime.core.MetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.emf.refactor.metrics.runtime.ui.MetricResultsView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class RuntimeManager {
	
	private static MetricCalculator calculator;
	private static LinkedList<Result> resultsViewInput;
	private static TableViewer resultsViewer = null;
	private static List<Action> additionalActions = new ArrayList<Action>();
	private static List<IHighlighting> additionalHighlightings = new ArrayList<IHighlighting>();
	
	private static RuntimeManager instance;
	
	private RuntimeManager() {
		calculator = MetricCalculator.getInstance();
		resultsViewInput = new LinkedList<Result>();
		System.out.println("RuntimeManager initialized!");
	}
	
	public static RuntimeManager getInstance() {
		if (instance == null) {
			instance = new RuntimeManager();
		}
		return instance;
	}
	
	public static RuntimeManager getInstance(Action action) {
		if (instance == null) {
			instance = new RuntimeManager();
			additionalActions.add(action);
		} else {
			additionalActions.add(action);
		}
		return instance;
	}
	
	public static RuntimeManager getInstance(IHighlighting highlighting) {
		if (instance == null) {
			instance = new RuntimeManager();
			additionalHighlightings.add(highlighting);
		} else {
			additionalHighlightings.add(highlighting);
		}
		return instance;
	}
	
	public static void setResultsViewer(TableViewer viewer){
		resultsViewer = viewer;
	}
	
	public static LinkedList<Result> getResultsViewInput(){
		return resultsViewInput;
	}
	
	public static void calculateConfiguredMetricsOnElement(IProject project, List<EObject> context){
		ConfigurationManager.getInstance();
		Configuration configuration = ConfigurationManager.getConfiguration(project);
		calculator.setConfiguration(configuration);
		calculator.setContext(context);
		calculator.calculateMetrics();
		resultsViewInput.addAll(calculator.getResults());
		if(! isMetricsViewOpen()){
			createMetricsView();
		}
		if(resultsViewer != null && resultsViewInput != null) {
			resultsViewer.setInput(resultsViewInput);
		}
	}
	
	public static void calculateConfiguredMetricsTransitively(IProject project, EObject root) {
		ConfigurationManager.getInstance();
		Configuration configuration = ConfigurationManager.getConfiguration(project);
		calculator.setConfiguration(configuration);
		LinkedList<Result> results = new LinkedList<Result>();
		LinkedList<EObject> allEObjects = new LinkedList<EObject>();
		allEObjects.add(root);
		allEObjects.addAll(getAllEObjects(root));
		for(EObject currentEObject : allEObjects) {
			ArrayList<EObject> contextObjectList = new ArrayList<EObject>();
			contextObjectList.add(currentEObject);
			calculator.setContext(contextObjectList);
			calculator.calculateMetrics();
			results.addAll(calculator.getResults());
		}	
		if(!isMetricsViewOpen()) {
			createMetricsView();
		}
		if(resultsViewer != null && resultsViewInput != null) {
			resultsViewInput.addAll(results);
			resultsViewer.setInput(resultsViewInput);
		}
	}
	
	private static List<EObject> getAllEObjects(EObject root) {
		ArrayList<EObject> eObjects = new ArrayList<EObject>();
		List<EObject> directContents = root.eContents();
		eObjects.addAll(directContents);
		for(EObject contentObject : directContents){
			eObjects.addAll(getAllEObjects(contentObject));
		}
		return eObjects;
	}
	
	private static void createMetricsView(){
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		try{
			page.showView(MetricResultsView.ID);
			setAdditionalActionsToView(getMetricsView());
			setAdditionalHightightingsToView(getMetricsView());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	private static MetricResultsView getMetricsView() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		for(int i=0; i<page.getViewReferences().length; i++) {
			if(page.getViewReferences()[i].getId().equals(MetricResultsView.ID)) {
				return ((MetricResultsView) page.getViewReferences()[i].getView(true));
			}
		}
		return null;
	}
	
	private static boolean isMetricsViewOpen() {
		MetricResultsView view = getMetricsView();
		if (view != null) {
			setAdditionalActionsToView(view);
			setAdditionalHightightingsToView(getMetricsView());
			return true;
		}
		return false;
	}

	private static void setAdditionalActionsToView(MetricResultsView view) {
		for (Action action : additionalActions) {
			System.out.println("RuntimeManager: add action '" + action + "' to view!");
			view.addAction(action);
		}
		view.addActionsToMenu();
	}
	
	private static void setAdditionalHightightingsToView(MetricResultsView view) {
		for (IHighlighting highlighting : additionalHighlightings) {
			System.out.println("RuntimeManager: add highlighting '" + highlighting + "' to view!");
			view.addHighlighting(highlighting);
		}
	}
}
