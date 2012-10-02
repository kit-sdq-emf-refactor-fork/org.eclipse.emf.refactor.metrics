package org.eclipse.emf.refactor.metrics.generator.interfaces;

import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;

public interface INewMetricWizard {
	
	public void setTargetProject(String projectName);
	
	public LinkedList<IProject> getProjects();
	
	public void setName(String name);

	public void setId(String id);

	public void setDescription(String description);
	
	public void setMetamodel(String metamodel);

	public void setContext(String context);

	public void setJar(String jar);
	
	public int getPageNumbers();
	
	public WizardPage getSecondPage();

	public void setImportPackage(String importPackage);
	
	public void updateSecondPage();
	
	public void setMetamodelAndContext(String metamodel, String contextType);

}
