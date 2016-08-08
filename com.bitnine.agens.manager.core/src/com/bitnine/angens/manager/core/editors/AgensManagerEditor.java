package com.bitnine.angens.manager.core.editors;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
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
import com.bitnine.angens.manager.core.editors.parts.logs.LogComposite;
import com.bitnine.angens.manager.core.editors.parts.os.CPUUsageTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.DiskUsagePerTableSpaceTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.DiskUsageperTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.IoSizeTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.IoTimeTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.IoUsageTableComposite;
import com.bitnine.angens.manager.core.editors.parts.os.LoadAverageTableComposite;
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
import com.bitnine.angens.manager.core.editors.parts.statistics.DiskReadComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.InstanceProcessesRatioTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.InstanceProcessesTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.MemoryUsageComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.RecoveryConflictsTableComposite;
import com.bitnine.angens.manager.core.editors.parts.statistics.TableSizeComposite;
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

	/** 모니터링이 가능한지 유무 */
	protected boolean isMonitoringStart = false;
	/** 모니터링 대상 DB */
	private UserDBDAO userDB;

	/** 모니터링 인스턴스 combo */
	private Combo comboInstance;
	/** 디테일 오브젝트를 보여줄 composite */
	private CTabFolder tabFolderMainResult;

	/**
	 * 
	 */
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

		if (isMonitoringStart) {
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
			createLog();

			tabFolderMainResult.setSelection(0);
		}
	}
	
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

			isMonitoringStart = true;
		} catch (Exception e) {
			logger.error("get instance", e);

			MessageDialog.openError(null, "Error", "모니터링 테이블이 발견되지 않았습니다. 확인하여 주십시오.");
		}
	}

	/**
	 * get instace
	 * 
	 * @return
	 */
	private Instance getInstance() {
		Object obj = comboInstance.getData(comboInstance.getText());
		if (obj != null)
			return (Instance) obj;
		return null;
	}

	/**
	 * create summary
	 */
	private void createSummary() {
		CTabItem tbtmSummary = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmSummary.setText("Summary");

		SashForm compSummary = new SashForm(tabFolderMainResult, SWT.VERTICAL);
		tbtmSummary.setControl(compSummary);
		compSummary.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		SummaryComposite summaryComposite = new SummaryComposite(compSummary, userDB, getInstance());
		summaryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		AlertMessageComposite alertComposite = new AlertMessageComposite(compSummary, userDB, getInstance(), new AlertMessageLabelProvider());
		alertComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * create statistics
	 */
	private void createStatistics() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Statistics");

		final ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolderMainResult, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		tbtmStatistics.setControl(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(scrolledComposite);
		
		final Composite compositeBody = new Composite(scrolledComposite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));

		// database statistics table
		DatabaseStatisticsTableComposite databaseComposite = new DatabaseStatisticsTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		databaseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// transaction statistics table 
		TransactionStatisticsTableComposite transactionComposite = new TransactionStatisticsTableComposite(compositeBody, userDB, getInstance());
		transactionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// database size table 
		DatabaseSizeTableComposite databaseSizeComposite = new DatabaseSizeTableComposite(compositeBody, userDB, getInstance());
		databaseSizeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// recovery conflicts
		RecoveryConflictsTableComposite recoveryConflictsComposite = new RecoveryConflictsTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		recoveryConflictsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//
		// instance activity
		// WAL statistic table
		WALStatisticsStatsTableComposite walStatsComposite = new WALStatisticsStatsTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		walStatsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// WAL statistic table
		WALStatisticsTableComposite walConflictsComposite = new WALStatisticsTableComposite(compositeBody, userDB, getInstance());
		walConflictsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// instance process ratio
		InstanceProcessesRatioTableComposite instanceProcessRatioComposite = new InstanceProcessesRatioTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		instanceProcessRatioComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// instance process
		InstanceProcessesTableComposite instanceProcessComposite = new InstanceProcessesTableComposite(compositeBody, userDB, getInstance());
		instanceProcessComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// initialize scrolled composite
		scrolledComposite.setContent(compositeBody);
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = compositeBody.getClientArea();
				scrolledComposite.setMinSize(compositeBody.computeSize(r.width, SWT.DEFAULT));
			}
		});
		
	}

	/**
	 * create OS
	 */
	private void createOS() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("OS");
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolderMainResult, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		tbtmStatistics.setControl(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(scrolledComposite);

		final Composite compositeBody = new Composite(scrolledComposite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));

		CPUUsageTableComposite cpuUsageTableComposite = new CPUUsageTableComposite(compositeBody, userDB, getInstance());
		cpuUsageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		LoadAverageTableComposite loadAverageTableComposite = new LoadAverageTableComposite(compositeBody, userDB, getInstance());
		loadAverageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IoUsageTableComposite ioUsageTableComposite = new IoUsageTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		ioUsageTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		IoSizeTableComposite ioSizeTableComposite = new IoSizeTableComposite(compositeBody, userDB, getInstance());
		ioSizeTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		IoTimeTableComposite ioTimeTableComposite = new IoTimeTableComposite(compositeBody, userDB, getInstance());
		ioTimeTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		MemoryUsageComposite memoryTableComposite = new MemoryUsageComposite(compositeBody, userDB, getInstance());
		memoryTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		DiskUsagePerTableSpaceTableComposite diskUseageperTablespaceTableComposite = new DiskUsagePerTableSpaceTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		diskUseageperTablespaceTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		DiskUsageperTableComposite diskUseageperTableComposite = new DiskUsageperTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		diskUseageperTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		 
		 // Table Size
		TableSizeComposite tableSizeComposite = new TableSizeComposite(compositeBody, userDB, getInstance());
		tableSizeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		 
		 // Disk Read
		DiskReadComposite diskReadComposite = new DiskReadComposite(compositeBody, userDB, getInstance());
		diskReadComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// initialize scrolled composite
		scrolledComposite.setContent(compositeBody);
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = compositeBody.getClientArea();
				scrolledComposite.setMinSize(compositeBody.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}

	/**
	 * create sql composite
	 */
	private void createSQL() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("SQL");
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolderMainResult, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		tbtmStatistics.setControl(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(scrolledComposite);

		final Composite compositeBody = new Composite(scrolledComposite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));

		HeavilyUpdatedTableComposite heavilyUpdatedTableComposite = new HeavilyUpdatedTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		heavilyUpdatedTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		HeavilyAccessedTableComposite heavilyAccessedTableComposite = new HeavilyAccessedTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		heavilyAccessedTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		LowDensityTableComposite lowDensityTableComposite = new LowDensityTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		lowDensityTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		FragmentedTableComposite fragmentTableTableComposite = new FragmentedTableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		fragmentTableTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		FunctionsComposite functionTableComposite = new FunctionsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		functionTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		StatementsComposite statementsComposite = new StatementsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		statementsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		LongTransactionsComposite longTransaction = new LongTransactionsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		longTransaction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		LockConflictsComposite lockConflicts = new LockConflictsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		lockConflicts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// initialize scrolled composite
		scrolledComposite.setContent(compositeBody);
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = compositeBody.getClientArea();
				scrolledComposite.setMinSize(compositeBody.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}

	/**
	 * create activities composite
	 */
	private void createActivities() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Activities");
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolderMainResult, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		tbtmStatistics.setControl(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(scrolledComposite);

		final Composite compositeBody = new Composite(scrolledComposite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));

		// activities composite start
		CheckpointActivityComposite checkpointActivityComposite = new CheckpointActivityComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		checkpointActivityComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		BasicStatistics_AverageComposite basicStatisticsComposite = new BasicStatistics_AverageComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		basicStatisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		VacuumCancelsComposite vaccumCancelsComposite = new VacuumCancelsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		vaccumCancelsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		IOStatistics_AverageComposite ioStatistcsAverageComposite = new IOStatistics_AverageComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		ioStatistcsAverageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		AnalyzeStatisticsComposite analyzeStatisticsComposite = new AnalyzeStatisticsComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		analyzeStatisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		CurrentReplicationStatusComposite currentReplicationComposite = new CurrentReplicationStatusComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		currentReplicationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ReplicationDelaysComposite replicationDelaysComposite = new ReplicationDelaysComposite(compositeBody, userDB, getInstance());
		replicationDelaysComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// initialize scrolled composite
		scrolledComposite.setContent(compositeBody);
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = compositeBody.getClientArea();
				scrolledComposite.setMinSize(compositeBody.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}

	/**
	 * create information composite
	 * 
	 */
	private void createInformation() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Information");
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolderMainResult, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		tbtmStatistics.setControl(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(scrolledComposite);

		final Composite compositeBody = new Composite(scrolledComposite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));

		// information composite
		TableComposite tableComposite = new TableComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		IndexComposite indexComposite = new IndexComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		indexComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ParametersComposite parameterComposite = new ParametersComposite(compositeBody, userDB, getInstance(), new AgensMAPLabelProvider());
		parameterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// initialize scrolled composite
		scrolledComposite.setContent(compositeBody);
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = compositeBody.getClientArea();
				scrolledComposite.setMinSize(compositeBody.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}
	
	/**
	 * create log composite
	 * 
	 */
	private void createLog() {
		CTabItem tbtmStatistics = new CTabItem(tabFolderMainResult, SWT.NONE);
		tbtmStatistics.setText("Log Viewer");
		
		final Composite compositeBody = new Composite(tabFolderMainResult, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		tbtmStatistics.setControl(compositeBody);

		// log composite
		LogComposite logComposite = new LogComposite(compositeBody, userDB, getInstance());
		logComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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
		AgensManagerEditorInput qei = (AgensManagerEditorInput) input;
		userDB = qei.getUserDB();

		setSite(site);
		setInput(input);
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
