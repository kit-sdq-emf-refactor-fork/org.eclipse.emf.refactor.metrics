package org.eclipse.emf.refactor.metrics.reporter.ui;

import org.eclipse.emf.refactor.metrics.reporter.managers.ReportManager;
import org.eclipse.jface.wizard.Wizard;

public class ReportingWizard extends Wizard {
	
	private ReportManager metricsReporter;
	private ReportingWizardPage page;

	public ReportingWizard(ReportManager metricsReporter) {
		setWindowTitle("EMF Quality Assurance - Metrics Reporting -");
		this.metricsReporter = metricsReporter;
	}
	
	public ReportManager getMetricsReporter() {
		return metricsReporter;
	}

	@Override
	public void addPages() {
		page = new ReportingWizardPage(this);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		metricsReporter.setDoc(page.btnDoc.getSelection());
		metricsReporter.setHtml(page.btnHtml.getSelection());
		metricsReporter.setOdp(page.btnOdp.getSelection());
		metricsReporter.setOds(page.btnOds.getSelection());
		metricsReporter.setOdt(page.btnOdt.getSelection());
		metricsReporter.setPdf(page.btnPdf.getSelection());
		metricsReporter.setPostcript(page.btnPostcript.getSelection());
		metricsReporter.setPpt(page.btnPpt.getSelection());
		metricsReporter.setXls(page.btnXls.getSelection());
		metricsReporter.setDesignName(page.getDesign());
		metricsReporter.setFileName(page.getDestination());		
		metricsReporter.doReport();
		return true;
	}
	
	@Override
	public boolean canFinish() {
		return page.isPageComplete();
	}

}
