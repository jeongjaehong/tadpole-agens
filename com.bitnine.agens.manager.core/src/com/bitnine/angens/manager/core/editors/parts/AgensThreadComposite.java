package com.bitnine.angens.manager.core.editors.parts;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.angens.manager.core.utils.AgensGraphDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * agens thread composite
 * 
 * @author hangum
 *
 */
public abstract class AgensThreadComposite extends Composite {
	private static final Logger logger = Logger.getLogger(AgensThreadComposite.class);
	
	/** server push session */
	final ServerPushSession spsInstance = new ServerPushSession();
	protected boolean isUIThreadRunning = false;
	
	protected UserDBDAO userDB;
	protected Instance instance;

	public AgensThreadComposite(Composite parent,  UserDBDAO userDB, Instance instance) {
		super(parent, SWT.NONE);
		
		this.userDB = userDB;
		this.instance = instance;
		
		// create default composite
		
		// start monitoring
		startInstanceMon();
	}

	/**
	 * start instance monitoring
	 */
	protected void startInstanceMon() {
		if(!isUIThreadRunning) {
			spsInstance.start();
			Thread bgThread = new Thread( startUIThread() );
			bgThread.setDaemon( true );
			bgThread.start();
		}
	}
	
	/**
	 * get last snap id
	 * 
	 * @return
	 * @throws Exception
	 */
	protected int getLastSnapId() throws Exception {
		return AgensManagerSQLImpl.getSnapshotInfo(userDB, instance);
	}
	
	/**
	 * start runnable
	 * 
	 * @return
	 */
	private Runnable startUIThread() {
		isUIThreadRunning = true;
		
		Runnable bgRunnable = new Runnable() {
			public void run() {
		    
				while(isUIThreadRunning) {
					if(null != getInstance()) {
					    try {
					    	refreshUI(getUIData());
					    } catch(Exception e) {
					    	logger.error("Job executing", e);
					    }
					}
				    
				    try {
						Thread.sleep(AgensGraphDefine.MONITORING_CYCLE_SEC * 50);// * 1000);								
					} catch(Exception e){}	
				}	// end while
			}	// end run
		};
		
		return bgRunnable;
	}
	
	public abstract List<?> getUIData() throws Exception;
	public abstract void refreshUI(List<?> listData);

	/**
	 * get instance
	 * @return
	 */
	public Instance getInstance() {
		return instance;
	}
	
	/**
	 * get userDB
	 * @return
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	@Override
	protected void checkSubclass() {
	}
	
	@Override
	public void dispose() {
		isUIThreadRunning = false;
		super.dispose();
	}

}
