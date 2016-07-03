//package com.bitnine.angens.manager.core.editors.parts;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.eclipse.rap.addons.chart.basic.DataItem;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//
//import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
//import com.bitnine.agens.manager.engine.core.dao.domain.Activity;
//import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
//import com.hangum.tadpole.commons.util.ColorsSWTUtils;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//
///**
// * memory chart
// * 
// * @author hangum
// *
// */
//public class ActivityComposite extends CPUComposite {
//	private static final Logger logger = Logger.getLogger(ActivityComposite.class);
//	
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param title 
//	 * @param userDB
//	 * @param instance
//	 */
//	public ActivityComposite(Composite parent, String title, UserDBDAO userDB, Instance instance) {
//		super(parent, title, userDB, instance);
//	}
//	
//	/**
//	 * refresh UI
//	 * @param listCPU
//	 */
//	public void refreshUI(List<?> listCpu) {
//		final List<DataItem> listDataItem = new ArrayList<>();
//		try {
//			if(listCpu.isEmpty()) {
//				listDataItem.add(new DataItem( 0, "Idle (0)", ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
//				listDataItem.add(new DataItem( 0, "Idle_in_xact (0)", ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
//				listDataItem.add(new DataItem( 0, "Waiting (0)", ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
//				listDataItem.add(new DataItem( 0, "Running (0)", ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
//				listDataItem.add(new DataItem( 0, "Max_backends (0)", ColorsSWTUtils.CAT10_COLORS[ 4 ] ));
//			} else {
//				Activity memory = (Activity)listCpu.get(0);
//				listDataItem.add(new DataItem( memory.getIdle(), 		String.format("Idle (%s)", memory.getIdle()), ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
//				listDataItem.add(new DataItem( memory.getIdle_in_xact(),String.format("Idle_in_xact (%s)", memory.getIdle_in_xact()), ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
//				listDataItem.add(new DataItem( memory.getWaiting(), 	String.format("Waiting (%s)", memory.getWaiting()), ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
//				listDataItem.add(new DataItem( memory.getRunning(),		String.format("Running (%s)", memory.getRunning()), ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
//				listDataItem.add(new DataItem( memory.getMax_backends(),String.format("Max_backends (%s)", memory.getMax_backends()), ColorsSWTUtils.CAT10_COLORS[ 4 ] ));
//			}
//			
//			final Display display = grpCpu.getDisplay();
//		    display.asyncExec( new Runnable() {
//		    	public void run() {
//		    		pieChart.setItems(listDataItem.toArray(new DataItem[]{}));
//		    	}
//		    } );
//			
//		} catch(Exception e) {
//			logger.error("initialize data", e);
//		}
//	}
//	
//	/**
//	 * get cpu data
//	 * @return
//	 * @throws Exception
//	 */
//	public List<?> getUIData() throws Exception {
//		return AgensManagerSQLImpl.getActivityInfo(userDB, instance);
//	}
//}
