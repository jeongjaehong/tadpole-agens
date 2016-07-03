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
// * WAL Statistics composite
// * 
// * @author hangum
// *
// */
//public class WALStatisticsTableComposite extends AgensTableComposite {
//	private static final Logger logger = Logger.getLogger(WALStatisticsTableComposite.class);
//	
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param title
//	 * @param userDB
//	 * @param instance
//	 */
//	public WALStatisticsTableComposite(Composite parent, UserDBDAO userDB, Instance instance, LabelProvider labelProvider) {
//		super(parent, "WAL Statistics", userDB, instance, labelProvider);
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
//		String[] columnName = {"write_total", "write_speed"};
//		int[] columnSize = {100, 100};
//		int[] align = {SWT.RIGHT, SWT.RIGHT};
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