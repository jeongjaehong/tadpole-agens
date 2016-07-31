package com.bitnine.angens.manager.core.editors.parts.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.rap.addons.chart.basic.DataGroup;
import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.rap.addons.chart.basic.LineChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.angens.manager.core.editors.parts.AgensChartComposite;
import com.hangum.tadpole.commons.util.ColorsSWTUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Line transaction composite
 * 
 * @author hangum
 *
 */
public class LineTransactionComposite extends AgensChartComposite {
	private static final Logger logger = Logger.getLogger(LineTransactionComposite.class);
	/** data group */
	private Map<String, Double[]> mapDataGroup = new HashMap<>();
	private int position = 0;
	
	/** define line chart */
	protected LineChart lineChart;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public LineTransactionComposite(Composite parent, UserDBDAO userDB, Instance instance) {
		super(parent, userDB, instance);
		setLayout(new GridLayout(1, false));

		Group grpTransactionStatistics = new Group(this, SWT.NONE);
		grpTransactionStatistics.setLayout(new GridLayout(1, false));
		GridData gd_grpTransactionStatistics = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpTransactionStatistics.minimumHeight = 200;
		gd_grpTransactionStatistics.minimumWidth = 200;
		gd_grpTransactionStatistics.heightHint = 200;
		gd_grpTransactionStatistics.widthHint = 200;
		grpTransactionStatistics.setLayoutData(gd_grpTransactionStatistics);
		grpTransactionStatistics.setText("Memory Usage");

		lineChart = new LineChart(grpTransactionStatistics, SWT.NONE);
		lineChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		lineChart.setXAxisLabel("Timestamp");
		lineChart.setXAxisFormat("d3.format(',r')");

		lineChart.setYAxisLabel("Size (Bytes)");
		lineChart.setYAxisFormat("d");
//		lineChart.addListener(SWT.Selection, new Listener() {
//			@Override
//			public void handleEvent(Event event) {
//				if(logger.isDebugEnabled()) logger.debug("Selected line item #" + event.index + ", point #" + event.detail);
//			}
//		});

		initializeUIData();
	}
	
	/**
	 * initialize ui data
	 */
	private void initializeUIData() {
	}
	
	/**
	 * refresh UI
	 * @param listCPU
	 */
	public void refreshUI(final List<?> listData) {
		try {
			final Display display = lineChart.getDisplay();
		    display.asyncExec( new Runnable() {
		    	public void run() {
		   			lineChart.setItems(listData.toArray(new DataGroup[listData.size()]));
		    	}
		    } );
			
		} catch(Exception e) {
			logger.error("initialize data", e);
		}
	}

	/**
	 * get chart data
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getUIData() throws Exception {
		List<Map> listLine = AgensManagerSQLImpl.getSQLMapQueryInfo(getUserDB(), "memory_usage", getLastSnapId());
		if(logger.isDebugEnabled()) logger.debug("===### data size is : " + listLine.size());
		if (listLine.isEmpty()) return new ArrayList<>();

		int tmpPosition = position++;
		
		// "memfree", "buffers", "cached", "swap", "dirty"
		Map mapData = listLine.get(0);
		List<DataGroup> listDataGroup = new ArrayList<>(); 
		listDataGroup.add(new DataGroup(caclDoublePosition(tmpPosition, "memfree", 	mapData), "commit_tps", ColorsSWTUtils.CAT10_COLORS[1]));
		listDataGroup.add(new DataGroup(caclDoublePosition(tmpPosition, "buffers", 	mapData), "buffers", 	ColorsSWTUtils.CAT10_COLORS[2]));
		listDataGroup.add(new DataGroup(caclDoublePosition(tmpPosition, "cached", 	mapData), "cached", 	ColorsSWTUtils.CAT10_COLORS[3]));
		listDataGroup.add(new DataGroup(caclDoublePosition(tmpPosition, "swap", 	mapData), "swap", 		ColorsSWTUtils.CAT10_COLORS[4]));
		listDataGroup.add(new DataGroup(caclDoublePosition(tmpPosition, "dirty", 	mapData), "dirty", 		ColorsSWTUtils.CAT10_COLORS[5]));
		
		return listDataGroup;
	}
	
	/**
	 * data를 계산하여 보여준다.
	 * 
	 * @param key
	 * @param mapData
	 * @return
	 */
	protected DataItem[] caclDoublePosition(int tmpPosition, String key, Map mapData) {
		double dblValue = ((BigDecimal) mapData.get(key)).doubleValue();
		Double[] arryDouble = mapDataGroup.get(key);
		if(arryDouble == null) {
			arryDouble = initializeDblArry(0);
		}
		
		if(tmpPosition > 50) {
			position=51;
			Double[] arryTmp ={}; 
					
			System.arraycopy(arryDouble, 1, arryTmp, 0, 49);
			
			arryDouble = arryTmp;
			arryDouble[49] = dblValue;
		} else {
			arryDouble[tmpPosition] = dblValue;	
		}
		mapDataGroup.put(key, arryDouble);
		// time
		String strTime = ""+mapData.get("replace");
		
		List<DataItem> listItem = new ArrayList<>();
		for (Double double1 : arryDouble) {
			listItem.add(new DataItem(double1, strTime));
		}
		
		return listItem.toArray(new DataItem[listItem.size()]);
	}
	
	/**
	 * initialize double value
	 * @param initValue
	 * @return
	 */
	protected Double[] initializeDblArry(double initValue) {
		Double[] arryDbl = new Double[50];
		for(int i=0; i<50; i++) {
			arryDbl[i] = 0d;
		}
		arryDbl[0] = initValue;
		
		return arryDbl;
	}
}
