package com.bitnine.agens.manager.engine.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bitnine.agens.manager.engine.core.dao.domain.Activity;
import com.bitnine.agens.manager.engine.core.dao.domain.AlertMessage;
import com.bitnine.agens.manager.engine.core.dao.domain.Cpu;
import com.bitnine.agens.manager.engine.core.dao.domain.Database;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.agens.manager.engine.core.dao.domain.Memory;
import com.bitnine.agens.manager.engine.core.dao.domain.Tablespace;
import com.bitnine.agens.manager.engine.core.dao.domain.custom.InstanceProcessRation;
import com.bitnine.agens.manager.engine.core.dao.domain.custom.WALStatistics;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * agens amanger sql
 * 
 * @author hangum
 *
 */
public class AgensManagerSQLImpl {
	private static final Logger logger = Logger.getLogger(AgensManagerSQLImpl.class);

	/**
	 * get instance list 
	 * 
	 * @param userDB
	 */
	public static List<Instance> getInstanceInfo(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listInstance");
	}
	
	/**
	 * get alert message
	 * 
	 * @param userDB
	 * @param instance
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<AlertMessage> getAlertMessageInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listAlertMessage", instance);
	}
	
	/**
	 * get Database information
	 * 
	 * @param userDB
	 * @param instance
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<Database> getDatabaseInfo(UserDBDAO userDB, int snapid) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listDatabase", snapid);
	}
	
//	/**
//	 * get cpu information
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 * @throws TadpoleSQLManagerException
//	 * @throws SQLException
//	 */
//	public static List<Cpu> getCPUInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//		return sqlClient.queryForList("listCPU", instance);
//	}
//
//	/**
//	 * get Memory information
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 * @throws TadpoleSQLManagerException
//	 * @throws SQLException
//	 */
//	public static List<Memory> getMemoryInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//		return sqlClient.queryForList("listMemory", instance);
//	}
//	
//	/**
//	 * get activity information
//	 *  
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 * @throws TadpoleSQLManagerException
//	 * @throws SQLException
//	 */
//	public static List<Activity> getActivityInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//		return sqlClient.queryForList("listActivity", instance);
//	}
	
//
//	/**
//	 * get WAL statistics
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 */
//	public static List<WALStatistics> getWALStatistics(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		return new ArrayList<WALStatistics>();
//	}
//	
//	/**
//	 * get Instance ProcessesRatio
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 */
//	public static List<InstanceProcessRation> getInstanceProcessesRatio(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		return new ArrayList<InstanceProcessRation>();
//	}
//
//	/**
//	 * get Disk Usageper Table Space
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 */
//	public static List<Tablespace> getDiskUsageperTableSpace(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//		return sqlClient.queryForList("listDiskUsageperTableSpace", instance);
//	}
//
//	/**
//	 * get Disk Usageper Table
//	 * 
//	 * @param userDB
//	 * @param instance
//	 * @return
//	 */
//	public static List<?> getDiskUsageperTable(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//		return sqlClient.queryForList("listDiskUsageperTable", instance);
//	}
}
