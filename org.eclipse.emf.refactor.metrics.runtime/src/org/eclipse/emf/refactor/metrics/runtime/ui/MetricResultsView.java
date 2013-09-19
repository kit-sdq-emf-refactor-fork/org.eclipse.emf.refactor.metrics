package org.eclipse.emf.refactor.metrics.runtime.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.refactor.metrics.interfaces.IHighlighting;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.emf.refactor.metrics.runtime.managers.RuntimeManager;
import org.eclipse.emf.refactor.metrics.runtime.ui.actions.ClearAction;
import org.eclipse.emf.refactor.metrics.runtime.ui.actions.SaveAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MetricResultsView extends ViewPart {
	
	private static final String RESULT_COLUMN_LABEL = "Result";

	private static final String DESCRIPTION_COLUMN_LABEL = "Description";

	private static final String METRIC_COLUMN_LABEL = "Metric";

	private static final String CONTEXT_COLUMN_LABEL = "Context";

	private static final String TIME_COLUMN_LABEL = "Time";
	
	private static IMenuManager barMM;
	private static IToolBarManager toolbarMM;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.emf.refactor.metrics.view";
	private TableViewer viewer;
	private Composite parent;
	private SaveAction saveAction;
	private Action clearAction;
	private List<Action> additionalActions = new ArrayList<Action>();
	private List<IHighlighting> additionalHighlightings = new ArrayList<IHighlighting>();

	public MetricResultsView() { }
	
	public void addAction(Action action) {
		additionalActions.add(action);
	}
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {
		RuntimeManager.getInstance();
		this.parent = parent;
		TableColumn column;
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		final Table table = viewer.getTable();
		RuntimeManager.setResultsViewer(viewer);
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(CONTEXT_COLUMN_LABEL);
		column.setWidth(200);
		column.addListener(SWT.Selection, new ColumnSortListener(column));
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(METRIC_COLUMN_LABEL);
		column.setWidth(120);
		column.addListener(SWT.Selection, new ColumnSortListener(column));
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(DESCRIPTION_COLUMN_LABEL);
		column.setWidth(360);
		column.addListener(SWT.Selection, new ColumnSortListener(column));
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(RESULT_COLUMN_LABEL);
		column.setWidth(70);
		column.addListener(SWT.Selection, new ColumnSortListener(column));
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(TIME_COLUMN_LABEL);
		column.setWidth(150);
		column.addListener(SWT.Selection, new ColumnSortListener(column));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				doAdditionalHighlightings(null);
			}
			
			@Override
			public void focusGained(FocusEvent e) {				
			}
		});
		
		viewer.setContentProvider(new MetricResultsViewContentProvider());
		viewer.setLabelProvider(new MetricResultsViewLabelProvider());
		viewer.setInput(RuntimeManager.getResultsViewInput());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("===>>> event: " + event);
				if(event.getSelection().isEmpty()) {
					return;
			    }
				IWorkbenchWindow workbenchWindow= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IEditorPart editorPart = workbenchWindow.getActivePage().getActiveEditor();
				if (event.getSelection() instanceof IStructuredSelection) {
					StructuredSelection ss = (StructuredSelection) event.getSelection();
					doAdditionalHighlightings(ss);
				}
				System.out.println("editorPart: " + editorPart);
				System.out.println("editorPart.class: " + editorPart.getClass());
				if(editorPart != null && editorPart instanceof IViewerProvider) {
					setSelectionInModel((IViewerProvider)editorPart, (IStructuredSelection)event.getSelection());
				}				
			}
			
			private void setSelectionInModel(IViewerProvider provider, IStructuredSelection selection){
				ArrayList<EObject> eObjectList = new ArrayList<EObject>();
				for(Object selectedObject : selection.toList()){
					if(selectedObject instanceof Result){
						for(EObject currentEObject : ((Result) selectedObject).getContext()) {
							EObject modelInstanceObject = getModelObjectInstance(provider, currentEObject);
							eObjectList.add(modelInstanceObject);
						}
					}
				}
				provider.getViewer().setSelection(new StructuredSelection(eObjectList));				
			}
			
			private EObject getModelObjectInstance(IViewerProvider provider, EObject eObject) {
				Viewer viewer = provider.getViewer();
				TreeViewer treeViewer = (TreeViewer)viewer;
				Tree tree = treeViewer.getTree();
				TreeItem treeItem = tree.getItem(0);
				Resource o = (Resource)treeItem.getData();
				ResourceSet set = o.getResourceSet();
				EObject ob = set.getEObject(EcoreUtil.getURI(eObject), true);
				return ob;
			}
			
		});
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),"EMFMetrics.viewer");
		makeActions();
//		hookContextMenu();
		//hookDoubleClickAction();
		contributeToActionBars();
	}

//	private void hookContextMenu() {
//		MenuManager menuMgr = new MenuManager("#PopupMenu");
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				MetricResultsView.this.fillContextMenu(manager);
//			}
//		});
//		Menu menu = menuMgr.createContextMenu(viewer.getControl());
//		viewer.getControl().setMenu(menu);
//		getSite().registerContextMenu(menuMgr, viewer);
//	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		barMM = manager;
		manager.add(saveAction);
		manager.add(clearAction);
		manager.add(new Separator());
	}

//	private void fillContextMenu(IMenuManager manager) {
//		contextMM = manager;
//		manager.add(saveAction);
//		manager.add(clearAction);
//		// Other plug-ins can contribute there actions here
////		for (Action action : additionalActions) {
////			manager.add(action);
////		}
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//	}

	private void fillLocalToolBar(IToolBarManager manager) {
		toolbarMM = manager;
		manager.add(saveAction);
		manager.add(clearAction);
	}
	
	public void addActionsToMenu() {
		for (Action action : additionalActions) {	
			if (toolbarMM.getItems().length != additionalActions.size()+2) {
				barMM.add(action);
				barMM.update(true);
				toolbarMM.add(action);
				toolbarMM.update(true);
			}
		}
		additionalActions.clear();
	}

	private void makeActions() {
		saveAction = new SaveAction(parent.getShell(), viewer);		
		clearAction = new ClearAction(viewer);		
	}
	
	private class ColumnSortListener implements Listener {
		private TableColumn column;
		public ColumnSortListener(TableColumn column) {
			this.column = column;
		}
		
		private void orderByColumn(TableColumn column) {
			MetricResultsViewContentProvider provider = (MetricResultsViewContentProvider)viewer.getContentProvider();
			if(column.getText().equals(METRIC_COLUMN_LABEL))
				provider.sortContent(MetricResultsViewContentProvider.METRIC_NAME);
			if(column.getText().equals(RESULT_COLUMN_LABEL))
				provider.sortContent(MetricResultsViewContentProvider.VALUE);
			if(column.getText().equals(DESCRIPTION_COLUMN_LABEL))
				provider.sortContent(MetricResultsViewContentProvider.DESCRIPTION);
			if(column.getText().equals(TIME_COLUMN_LABEL))
				provider.sortContent(MetricResultsViewContentProvider.TIME);
			if(column.getText().equals(CONTEXT_COLUMN_LABEL))
				provider.sortContent(MetricResultsViewContentProvider.CONTEXT);
		}

		@Override
		public void handleEvent(Event event) {
			orderByColumn(column);		
		}
	}

	public void addHighlighting(IHighlighting highlighting) {
		additionalHighlightings.add(highlighting);
	}
	
	protected void doAdditionalHighlightings(Object selection) {
		for (IHighlighting highlighting : additionalHighlightings) {
			highlighting.highlight(selection);
		}
	}

}
