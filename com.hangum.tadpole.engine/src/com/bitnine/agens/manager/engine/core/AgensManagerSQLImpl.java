package com.bitnine.agens.manager.engine.core;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.bitnine.agens.manager.engine.core.dao.domain.Activity;
import com.bitnine.agens.manager.engine.core.dao.domain.AlertMessage;
import com.bitnine.agens.manager.engine.core.dao.domain.Cpu;
import com.bitnine.agens.manager.engine.core.dao.domain.Database;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.agens.manager.engine.core.dao.domain.Memory;
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
	 * get cpu information
	 * 
	 * @param userDB
	 * @param instance
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<Cpu> getCPUInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listCPU", instance);
	}

	/**
	 * get Memory information
	 * 
	 * @param userDB
	 * @param instance
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<Memory> getMemoryInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listMemory", instance);
	}
	
	/**
	 * get activity information
	 *  
	 * @param userDB
	 * @param instance
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<Activity> getActivityInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listActivity", instance);
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
	public static List<Database> getDatabaseInfo(UserDBDAO userDB, Instance instance) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listDatabase", instance);
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
}
