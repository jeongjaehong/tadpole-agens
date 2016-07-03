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
// * Disk Usage per Tablespace Composite
// * 
// * @author hangum
// *
// */
//public class DiskUsageperTableSpaceTableComposite extends AgensTableComposite {
//	private static final Logger logger = Logger.getLogger(DiskUsageperTableSpaceTableComposite.class);
//	
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param title
//	 * @param userDB
//	 * @param instance
//	 */
//	public DiskUsageperTableSpaceTableComposite(Composite parent, UserDBDAO userDB, Instance instance, LabelProvider labelProvider) {
//		super(parent, "Disk Usage per Tablespace", userDB, instance, labelProvider);
//	}
//	
//	/**
//	 * @return
//	 * @throws Exception
//	 */
//	public List<?> getUIData() throws Exception {
//		return AgensManagerSQLImpl.getDiskUsageperTableSpace(userDB, instance);
//	}
//	
//	/**
//	 * make columns
//	 */
//	public void createTableColumn() {
//		String[] columnName = {"tablespace", "location", "device", "used(MiB)", "avail(MiB)", "remain(%s)"};
//		int[] columnSize = {150, 200, 80, 80, 80, 80};
//		int[] align = {SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT};
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