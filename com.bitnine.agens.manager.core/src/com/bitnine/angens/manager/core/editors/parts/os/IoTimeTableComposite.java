package com.bitnine.angens.manager.core.editors.parts.os;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.angens.manager.core.editors.parts.AgensTableComposite;
import com.bitnine.angens.manager.core.editors.parts.lableprovider.AgensMAPLabelProvider;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * IO time table composite
 * 
 * @author hangum
 *
 */
public class IoTimeTableComposite extends AgensTableComposite {
	private static final Logger logger = Logger.getLogger(IoTimeTableComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param title
	 * @param userDB
	 * @param instance
	 */
	public IoTimeTableComposite(Composite parent, UserDBDAO userDB, Instance instance, AgensMAPLabelProvider labelProvider) {
		super(parent, "I/O Time", userDB, instance, labelProvider);
	}
	
	/**
	 * get cpu data
	 * @return
	 * @throws Exception
	 */
	public List<?> getUIData() throws Exception {
		return AgensManagerSQLImpl.getSQLMapQueryInfo(userDB, "io_time", getLastSnapId());
	}
	
	/**
	 * make columns
	 */
	public void createTableColumn() {
		String[] columnName = {"device_name", "avg read time", "avg write time"};
		int[] columnSize = {100, 80, 80};
		int[] align = {SWT.LEFT, SWT.RIGHT, SWT.RIGHT};
		
		for(int i=0; i<columnName.length; i++) {
			final TableViewerColumn tableColumn = new TableViewerColumn(tableView, align[i]);
			tableColumn.getColumn().setText(columnName[i]);
			tableColumn.getColumn().setWidth(columnSize[i]);
			tableColumn.getColumn().setAlignment(columnSize[i]);
			tableColumn.getColumn().setResizable(true);
			tableColumn.getColumn().setMoveable(false);
		}
	}
}