package com.bitnine.angens.manager.core.editors;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.angens.manager.core.editors.parts.AlertMessageComposite;
import com.bitnine.angens.manager.core.editors.parts.DatabaseTableComposite;
import com.bitnine.angens.manager.core.editors.parts.RecoveryConflictsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.SummaryComposite;
import com.bitnine.angens.manager.core.editors.parts.lableprovider.AlertMessageLabelProvider;
import com.bitnine.angens.manager.core.editors.parts.lableprovider.DatabaseLabelProvider;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * agens manager editor
 * 
 * @author hangum
 *
 */
public class AgensManagerEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(AgensManagerEditor.class);
	public static final String ID = "com.bitnine.angens.manager.core.editor.main";
	private UserDBDAO userDB;
	
	private Combo comboInstance;
//	private Group grpDashboard;
	private CTabFolder tabFolderMainResult;

	public AgensManagerEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Group grpConfiguration = new Group(parent, SWT.NONE);
		grpConfiguration.setLayout(new GridLayout(2, false));
		grpConfiguration.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		grpConfiguration.setText("Configuration");
		
		Label lblInstance = new Label(grpConfiguration, SWT.NONE);
		lblInstance.setText("Instance");
		
		comboInstance = new Combo(grpConfiguration, SWT.READ_ONLY);
		GridData gd_comboInstance = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboInstance.widthHint = 100;
		comboInstance.setLayoutData(gd_comboInstance);
		
		initUI();
		
		Group grpDashboard = new Group(parent, SWT.NONE);
		grpDashboard.setLayout(new GridLayout(1, false));
		grpDashboard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpDashboard.setText("Dashboard");
		
		tabFolderMainResult = new CTabFolder(grpDashboard, SWT.NONE);
		tabFolderMainResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createSummary();
		createStatistics();
		

		tabFolderMainResult.setSelection(0);
	}
	
//	/**
//	 * add chart
//	 */
//	private void addChart() {
//		Instance instance = (Instance)comboInstance.getData(comboInstance.getText());
//		String monitoringType = comboMonitoringType.getText();
//
//		AgensThreadComposite chartComposite = null;
//		if(monitoringType.equalsIgnoreCase("Activity")) {
//			chartComposite  = new ActivityComposite(grpDashboard, "Activity", userDB, instance);
//			chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		} else if(monitoringType.equalsIgnoreCase("Alert Message")) {
//			chartComposite  = new AlertMessageComposite(grpDashboard, "Alert Message", userDB, instance);
//			chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		} else if(monitoringType.equalsIgnoreCase("cpu")) {
//			chartComposite  = new CPUComposite(grpDashboard, "CPU", userDB, instance);
//			chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		} else if(monitoringType.equalsIgnoreCase("database")) {
//			chartComposite  = new DatabaseComposite(grpDashboard, "Database", userDB, instance);
//			chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
//		} else if(monitoringType.equalsIgnoreCase("memory")) {
//			chartComposite  = new MemoryComposite(grpDashboard, "Memory", userDB, instance);
//			chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		}
//		chartComposite.setLayout(new GridLayout(1, false));
//		chartComposite.getLayout();
//		grpDashboard.layout();
//	}
//	
	/**
	 * Initialize user interface
	 */
	private void initUI() {
		try {
			List<Instance> listInstance = AgensManagerSQLImpl.getInstanceInfo(userDB);
			
			for (Instance instance : listInstance) {
				String strName = instance.getHostname() + ":" + instance.getPort();
				comboInstance.add(strName);
				comboInstance.setData(strName, instance);
			}
			comboInstance.select(0);
		} catch (Exception e) {
			logger.error("get instance", e);
			
			MessageDialog.openError(null, "Error", "모니터링 테이블이 발견되지 않았습니다. 확인하여 주십시오.");
		}
	}
	
	/**
	 * get instace
	 * @return
	 */
	private Instance getInstance() {
		Object obj = comboInstance.getData(comboInstance.getText());
		if(obj != null) return (Instance)obj;
		return null;
	}
	
	/**
	 * create summary
	 */
	private void createSummary() {
		CTabItem tbtmSummary = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmSummary.setText("Summary");
		
		Composite compSummary = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmSummary.setControl(compSummary);
		compSummary.setLayout(new GridLayout(1, false));
		
		SummaryComposite summaryComposite = new SummaryComposite(compSummary, userDB, getInstance());
		summaryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		AlertMessageComposite alertComposite  = new AlertMessageComposite(compSummary, userDB, getInstance(), new AlertMessageLabelProvider());
		alertComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	/**
	 * create statistics
	 */
	private void createStatistics() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Statistics");
		
		Composite compStatistics = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setControl(compStatistics);
		compStatistics.setLayout(new GridLayout(1, false));
		
		DatabaseTableComposite databaseComposite  = new DatabaseTableComposite(compStatistics, userDB, getInstance(), new DatabaseLabelProvider());
		databaseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		RecoveryConflictsTableComposite recoveryConflictsComposite = new RecoveryConflictsTableComposite(compStatistics, userDB, getInstance(), new DatabaseLabelProvider());
		recoveryConflictsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		AgensManagerEditorInput qei = (AgensManagerEditorInput)input;
		userDB = qei.getUserDB();

		setSite(site);
		setInput(input);
		setPartName("Agens manager");
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
