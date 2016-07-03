//package com.bitnine.angens.manager.core.editors.parts;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.eclipse.rap.addons.chart.basic.DataItem;
//import org.eclipse.rap.addons.chart.basic.PieChart;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Group;
//
//import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
//import com.bitnine.agens.manager.engine.core.dao.domain.Cpu;
//import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
//import com.hangum.tadpole.commons.util.ColorsSWTUtils;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//
///**
// * cpu chart
// * 
// * @author hangum
// *
// */
//public class CPUComposite extends AgensThreadComposite {
//	private static final Logger logger = Logger.getLogger(CPUComposite.class);
//	protected Group grpCpu;
//	protected PieChart pieChart;
//	
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param title
//	 * @param userDB
//	 * @param instance
//	 */
//	public CPUComposite(Composite parent, String title, UserDBDAO userDB, Instance instance) {
//		super(parent, userDB, instance);
//		
//		grpCpu = new Group(this, SWT.NONE);
//		grpCpu.setLayout(new GridLayout(1, false));
//		grpCpu.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpCpu.setText(title);
//		
//		pieChart = new PieChart(grpCpu, SWT.NONE);
//		GridLayout gl_grpConnectionInfo = new GridLayout(1, false);
//		gl_grpConnectionInfo.verticalSpacing = 0;
//		gl_grpConnectionInfo.horizontalSpacing = 0;
//		gl_grpConnectionInfo.marginHeight = 0;
//		gl_grpConnectionInfo.marginWidth = 0;
//		pieChart.setLayout(gl_grpConnectionInfo);
//		pieChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		try {
//			refreshUI(getUIData());
//		} catch(Exception e) {
//			logger.error("refresh UI", e);
//		}
//		
//		startInstanceMon();
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
//				listDataItem.add(new DataItem( 0, "user (0)", ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
//				listDataItem.add(new DataItem( 0, "system (0)", ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
//				listDataItem.add(new DataItem( 0, "idle (0)", ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
//				listDataItem.add(new DataItem( 0, "iowait (0)", ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
//			} else {
//				Cpu cpu = (Cpu)listCpu.get(0);
//				listDataItem.add(new DataItem( cpu.getCpu_user(), 	String.format("user (%s)", cpu.getCpu_user()), ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
//				listDataItem.add(new DataItem( cpu.getCpu_system(),String.format("system (%s)", cpu.getCpu_system()), ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
//				listDataItem.add(new DataItem( cpu.getCpu_idle(), 	String.format("idle (%s)", cpu.getCpu_idle()), ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
//				listDataItem.add(new DataItem( cpu.getCpu_iowait(),String.format("iowait (%s)", cpu.getCpu_iowait()), ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
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
//		return AgensManagerSQLImpl.getCPUInfo(userDB, instance);
//	}
//	
//}
