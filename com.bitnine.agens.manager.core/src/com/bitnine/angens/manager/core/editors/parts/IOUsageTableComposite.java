//package com.bitnine.angens.manager.core.editors.parts;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jface.viewers.LabelProvider;
//import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Composite;
//
//import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
//import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//
///**
// * Instance Processes Ratio Composite
// * 
// * @author hangum
// *
// */
//public class IOUsageTableComposite extends AgensTableComposite {
//	private static final Logger logger = Logger.getLogger(IOUsageTableComposite.class);
//	
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param title
//	 * @param userDB
//	 * @param instance
//	 */
//	public IOUsageTableComposite(Composite parent, UserDBDAO userDB, Instance instance, LabelProvider labelProvider) {
//		super(parent, "Instance Processes Ratio", userDB, instance, labelProvider);
//	}
//	
//	/**
//	 * get cpu data
//	 * @return
//	 * @throws Exception
//	 */
//	public List<?> getUIData() throws Exception {
//		return AgensManagerSQLImpl.getWALStatistics(userDB, instance);
//	}
//	
//	/**
//	 * make columns
//	 */
//	public void createTableColumn() {
//		String[] columnName = {"device_name", "including TableSpaces", "total read(MiB)", "total write(MiB)", "total read time(ms)", "total write time(ms)", "current I/O(ms)" };
//		int[] columnSize = {80, 200, 80, 80, 80, 80, 80};
//		int[] align = {SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT};
//		
//		for(int i=0; i<columnName.length; i++) {
//			final TableViewerColumn tableColumn = new TableViewerColumn(tableView, align[i]);
//			tableColumn.getColumn().setText(columnName[i]);
//			tableColumn.getColumn().setWidth(columnSize[i]);
//			tableColumn.getColumn().setAlignment(columnSize[i]);
//			tableColumn.getColumn().setResizable(true);
//			tableColumn.getColumn().setMoveable(false);
//		}
//	}
//}