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
import com.bitnine.angens.manager.core.editors.parts.activities.AnalyzeStatisticsComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.BasicStatistics_AverageComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.CheckpointActivityComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.CurrentReplicationStatusComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.IOStatistics_AverageComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.ReplicationDelaysComposite;
import com.bitnine.angens.manager.core.editors.parts.activities.VacuumCancelsComposite;
import com.bitnine.angens.manager.core.editors.parts.information.IndexComposite;
import com.bitnine.angens.manager.core.editors.parts.information.ParametersComposite;
import com.bitnine.angens.manager.core.editors.parts.information.TableComposite;
import com.bitnine.angens.manager.core.editors.parts.lableprovider.AgensMAPLabelProvider;
import com.bitnine.angens.manager.core.editors.parts.lableprovider.AlertMessageLabelProvider;
import com.bitnine.angens.manager.core.editors.parts.os.IoSizeTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.IoTimeTableComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.FragmentedTableComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.FunctionsComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.HeavilyAccessedTableComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.HeavilyUpdatedTableComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.LockConflictsComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.LongTransactionsComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.LowDensityTableComposite;
import com.bitnine.angens.manager.core.editors.parts.sql.StatementsComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.DatabaseSizeTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.DatabaseStatisticsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.InstanceProcessesRatioTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.InstanceProcessesTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.LineTransactionComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.RecoveryConflictsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.TransactionStatisticsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.WALStatisticsStatsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.WALStatisticsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.summary.AlertMessageComposite;
import com.bitnine.angens.manager.core.editors.parts.summary.SummaryComposite;
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
		createOS();
		createSQL();
		createActivities();
		createInformation();

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
		
		DatabaseStatisticsTableComposite databaseComposite  = new DatabaseStatisticsTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		databaseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TransactionStatisticsTableComposite transactionComposite  = new TransactionStatisticsTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		transactionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		DatabaseSizeTableComposite databaseSizeComposite  = new DatabaseSizeTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		databaseSizeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// recovery conflicts
		RecoveryConflictsTableComposite recoveryConflictsComposite = new RecoveryConflictsTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		recoveryConflictsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
		// instance activity
		// WAL statistic table
		WALStatisticsTableComposite walConflictsComposite = new WALStatisticsTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		walConflictsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// WAL statistic table
		WALStatisticsStatsTableComposite walStatsComposite = new WALStatisticsStatsTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		walStatsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// instance process ratio
		InstanceProcessesRatioTableComposite instanceProcessRatioComposite = new InstanceProcessesRatioTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		instanceProcessRatioComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// instance process 
		InstanceProcessesTableComposite instanceProcessComposite = new InstanceProcessesTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		instanceProcessComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	/**
	 * create statistics
	 */
	private void createOS() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("OS");
		
		Composite compStatistics = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setControl(compStatistics);
		compStatistics.setLayout(new GridLayout(1, false));
		
//		CPUUsageTableComposite cpuUsageTableComposite  = new CPUUsageTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
//		cpuUsageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		LoadAverageTableComposite loadAverageTableComposite  = new LoadAverageTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
//		loadAverageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		IoUsageTableComposite ioUsageTableComposite  = new IoUsageTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
//		ioUsageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IoSizeTableComposite ioSizeTableComposite  = new IoSizeTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		ioSizeTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IoTimeTableComposite ioTimeTableComposite  = new IoTimeTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		ioTimeTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		MemoryUsageTableComposite memoryTableComposite  = new MemoryUsageTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		LineTransactionComposite memoryTableComposite = new LineTransactionComposite(compStatistics, userDB, getInstance());
		memoryTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//
//		DiskUsagePerTableSpaceTableComposite diskUseageperTablespaceTableComposite  = new DiskUsagePerTableSpaceTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
//		diskUseageperTablespaceTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//
//		DiskUsageperTableComposite diskUseageperTableComposite  = new DiskUsageperTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
//		diskUseageperTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	 }
	
	/**
	 * sql
	 */
	private void createSQL() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("SQL");
		
		Composite compStatistics = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setControl(compStatistics);
		compStatistics.setLayout(new GridLayout(1, false));
		
		HeavilyUpdatedTableComposite heavilyUpdatedTableComposite  = new HeavilyUpdatedTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		heavilyUpdatedTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		HeavilyAccessedTableComposite heavilyAccessedTableComposite  = new HeavilyAccessedTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		heavilyAccessedTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		LowDensityTableComposite lowDensityTableComposite  = new LowDensityTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		lowDensityTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		FragmentedTableComposite fragmentTableTableComposite  = new FragmentedTableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		fragmentTableTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		FunctionsComposite functionTableComposite  = new FunctionsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		functionTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		StatementsComposite statementsComposite  = new StatementsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		statementsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		LongTransactionsComposite longTransaction = new LongTransactionsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		longTransaction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		LockConflictsComposite lockConflicts = new LockConflictsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		lockConflicts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	private void createActivities() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Activities");
		
		Composite compStatistics = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setControl(compStatistics);
		compStatistics.setLayout(new GridLayout(1, false));
		
		CheckpointActivityComposite checkpointActivityComposite  = new CheckpointActivityComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		checkpointActivityComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		BasicStatistics_AverageComposite basicStatisticsComposite  = new BasicStatistics_AverageComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		basicStatisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		VacuumCancelsComposite vaccumCancelsComposite  = new VacuumCancelsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		vaccumCancelsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IOStatistics_AverageComposite ioStatistcsAverageComposite  = new IOStatistics_AverageComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		ioStatistcsAverageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		AnalyzeStatisticsComposite analyzeStatisticsComposite  = new AnalyzeStatisticsComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		analyzeStatisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CurrentReplicationStatusComposite currentReplicationComposite  = new CurrentReplicationStatusComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		currentReplicationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ReplicationDelaysComposite replicationDelaysComposite  = new ReplicationDelaysComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		replicationDelaysComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	private void createInformation() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Information");
		
		Composite compStatistics = new Composite(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setControl(compStatistics);
		compStatistics.setLayout(new GridLayout(1, false));
		
		TableComposite tableComposite  = new TableComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IndexComposite indexComposite  = new IndexComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		indexComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ParametersComposite parameterComposite  = new ParametersComposite(compStatistics, userDB, getInstance(), new AgensMAPLabelProvider());
		parameterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
