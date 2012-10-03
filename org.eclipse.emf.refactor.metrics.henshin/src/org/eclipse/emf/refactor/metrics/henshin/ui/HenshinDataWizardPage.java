package org.eclipse.emf.refactor.metrics.henshin.ui;	

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.refactor.metrics.henshin.managers.HenshinFileManager;
import org.eclipse.emf.refactor.metrics.managers.FileManager;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class HenshinDataWizardPage extends WizardPage implements Listener {

	private static final String HENSHIN_EXT = ".henshin";
	private static final String PAGE_NAME = "org.eclipse.emf.refactor.metrics.HenshinDataWizardPage";
	private static final String TITLE = "New Metric: Henshin Data";
	private static final String DESCRIPTION = "Please specify the corresponding Henshin transformation file.";
	private Combo transformationCombo;
	private String transformation;
	
	public HenshinDataWizardPage() {
		super(PAGE_NAME);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
	}

	@Override
	public void createControl(Composite parent) {
		final Shell shell = parent.getShell();
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		setControl(container);
		createContent(container, shell);
		this.setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		updatePageComplete();
		getWizard().getContainer().updateButtons();
	}

	private void createContent(Composite container, Shell shell) {
		Label label;
		Button button;
		GridData gridData;
		Group group;
		GridLayout layout;
		group = new Group(container, SWT.NONE);
		group.setText("Henshin file");
		layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		
		// - Transf -
		label = new Label(group, SWT.NONE);
		label.setText("Metric calculation file:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		transformationCombo = new Combo(group, SWT.READ_ONLY);
		transformationCombo.addListener(SWT.Modify, this);
		transformationCombo
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button = new Button(group, SWT.NONE);
		button.addSelectionListener(new HenshinDataSelectionAdapter(shell,
				transformationCombo));
		button.setText("Import");
	}

	protected void initContents() {
		IProject project = ((NewMetricWizardHenshin) getWizard()).getTargetProject();
		String path = project.getLocationURI().getPath() + NewMetricWizardHenshin.TRANSFORMATIONS_DIR;
		setCombos(HenshinFileManager.getAllHenshinFiles(path));
	}

	private void setCombos(File[] files) {
		transformationCombo.removeAll();
		if (files != null) {
			for (File file : files) {
				transformationCombo.add(file.getName());
			}
		}
	}

	/**
	 * Überprüft die inhalte der Textfelder und erzeugt entschprechende
	 * Meldungen im Wizardfenster.
	 */
	private void updatePageComplete() {
		if (!checkTransformationTextField()) {
			setMessage("Metric calculation file not specified, " +
									"or file does not exist.", ERROR);
			this.setPageComplete(false);
			getWizard().getContainer().updateButtons();
		} else {
			setMessage("");
			saveContents();
			this.setPageComplete(true);
			getWizard().getContainer().updateButtons();
		}
	}

	private boolean checkTransformationTextField() {
		final String transf = this.transformationCombo.getText();
		if (!transf.isEmpty())
			return true;
		return false;
	}

	private void saveContents() {
		this.transformation = this.transformationCombo.getText();
	}

	public String getTransformation() {
		return transformation;
	}

	private void addToCombo(String name){
		transformationCombo.add(name,0);
	}
	

	// -------------------------------------------------------------------------------
	private class HenshinDataSelectionAdapter extends SelectionAdapter {

		private final Combo combo;
		private final Shell shell;

		public void widgetSelected(SelectionEvent e) {
			String result = browseForTransformation(shell);
			if (result != null)
				if (!result.isEmpty()) {
					result=result.replace('\\', '/');
					int i = result.lastIndexOf("/");
					String name = result.substring(i + 1);
					String destination = ((NewMetricWizardHenshin) getWizard()).getTargetProject().getLocationURI().getPath() 
											+ NewMetricWizardHenshin.TRANSFORMATIONS_DIR;
					FileManager.copyFile(result, destination, name);
					addToCombo(name);
					combo.select(0);
				}
		}

		private HenshinDataSelectionAdapter(Shell shell, Combo combo) {
			this.shell = shell;
			this.combo = combo;
		}

		private String browseForTransformation(Shell shell) {
			FileDialog fd = new FileDialog(shell, SWT.OPEN);
			fd.setText("Select Henshin file");
			if (((NewMetricWizardHenshin) getWizard()).getTargetProject() != null) {
				fd.setFilterPath(((NewMetricWizardHenshin) getWizard()).getTargetProject().getLocation().toString());
			}
			String[] filterExt = { "*" + HENSHIN_EXT };
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			return selected;
		}
	}

}
