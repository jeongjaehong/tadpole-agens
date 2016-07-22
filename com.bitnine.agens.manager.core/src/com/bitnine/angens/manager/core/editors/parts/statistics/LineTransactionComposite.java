package com.bitnine.angens.manager.core.editors.parts.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.rap.addons.chart.basic.DataGroup;
import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.rap.addons.chart.basic.LineChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.hangum.tadpole.commons.util.ColorsSWTUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Line transaction composite
 * 
 * @author hangum
 *
 */
public class LineTransactionComposite extends Composite {
	private static final Logger logger = Logger.getLogger(LineTransactionComposite.class);
	protected UserDBDAO userDB;
	protected Instance instance;
	protected LineChart lineChart;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public LineTransactionComposite(Composite parent, UserDBDAO userDB, Instance instance) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));

		this.userDB = userDB;
		this.instance = instance;

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
		lineChart.setXAxisFormat("t");

		lineChart.setYAxisLabel("Size (Bytes)");
		lineChart.setYAxisFormat("100f");
		lineChart.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("Selected line item #" + event.index + ", point #" + event.detail);
			}
		});

		Button button = new Button(grpTransactionStatistics, SWT.PUSH);
		button.setText("Change data");
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				updateLine();
			}
		});

	}

	private void updateLine() {
		try {
			lineChart.setItems(getUIData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private static DataGroup[] createItems() {
	// return new DataGroup[] { new DataGroup(createRandomPoints(), "Series 1",
	// ColorsSWTUtils.CAT10_COLORS[0]),
	// new DataGroup(createRandomPoints(), "Series 2",
	// ColorsSWTUtils.CAT10_COLORS[1]) };
	// }
	//
	// private static DataItem[] createRandomPoints() {
	// DataItem[] values = new DataItem[100];
	// for (int i = 0; i < values.length; i++) {
	// values[i] = new DataItem(Math.random() * 100);
	// }
	// return values;
	// }

	/**
	 * get cpu data
	 * 
	 * String[] columnName = {"timestamp", "datname", "commit_tps",
	 * "rollback_tps"};
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataGroup[] getUIData() throws Exception {
		List<Map> listLine = AgensManagerSQLImpl.getSQLMapQueryInfo(userDB, "memory_usage", getLastSnapId());
		logger.debug("===### data size is : " + listLine.size());
		if (listLine.isEmpty())
			return new DataGroup[] {};

		// "memfree", "buffers", "cached", "swap", "dirty"
		Map mapData = listLine.get(0);

		List<DataGroup> listDataGroup = new ArrayList<DataGroup>();
		// for (Map map : listLine) {
		BigDecimal commit_tps = (BigDecimal) mapData.get("memfree");
		listDataGroup.add(new DataGroup(new DataItem[] { new DataItem(commit_tps.doubleValue()) }, "commit_tps",
				ColorsSWTUtils.CAT10_COLORS[0]));

		BigDecimal rollback_tps = (BigDecimal) mapData.get("buffers");
		listDataGroup.add(new DataGroup(new DataItem[] { new DataItem(rollback_tps.doubleValue()) }, "rollback_tps",
				ColorsSWTUtils.CAT10_COLORS[1]));

		BigDecimal cached = (BigDecimal) mapData.get("cached");
		listDataGroup.add(new DataGroup(new DataItem[] { new DataItem(cached.doubleValue()) }, "cached",
				ColorsSWTUtils.CAT10_COLORS[2]));

		BigDecimal swap = (BigDecimal) mapData.get("swap");
		listDataGroup.add(new DataGroup(new DataItem[] { new DataItem(swap.doubleValue()) }, "swap",
				ColorsSWTUtils.CAT10_COLORS[3]));

		BigDecimal dirty = (BigDecimal) mapData.get("dirty");
		listDataGroup.add(new DataGroup(new DataItem[] { new DataItem(dirty.doubleValue()) }, "dirty",
				ColorsSWTUtils.CAT10_COLORS[4]));

		// }

		return listDataGroup.toArray(new DataGroup[listDataGroup.size()]);
	}

	protected int getLastSnapId() throws Exception {
		return AgensManagerSQLImpl.getSnapshotInfo(userDB, instance);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
