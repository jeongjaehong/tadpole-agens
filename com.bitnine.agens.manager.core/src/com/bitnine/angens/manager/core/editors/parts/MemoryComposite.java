package com.bitnine.angens.manager.core.editors.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.bitnine.agens.manager.engine.core.AgensManagerSQLImpl;
import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.agens.manager.engine.core.dao.domain.Memory;
import com.hangum.tadpole.commons.util.ColorsSWTUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * memory chart
 * 
 * @author hangum
 *
 */
public class MemoryComposite extends CPUComposite {
	private static final Logger logger = Logger.getLogger(MemoryComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param title 
	 * @param userDB
	 * @param instance
	 */
	public MemoryComposite(Composite parent, String title, UserDBDAO userDB, Instance instance) {
		super(parent, title, userDB, instance);
	}
	
	/**
	 * refresh UI
	 * @param listCPU
	 */
	public void refreshUI(List<?> listCpu) {
		final List<DataItem> listDataItem = new ArrayList<>();
		try {
			if(listCpu.isEmpty()) {
				listDataItem.add(new DataItem( 0, "Memfree (0)", ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
				listDataItem.add(new DataItem( 0, "Buffers (0)", ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
				listDataItem.add(new DataItem( 0, "Cached (0)", ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
				listDataItem.add(new DataItem( 0, "Swap (0)", ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
				listDataItem.add(new DataItem( 0, "Dirty (0)", ColorsSWTUtils.CAT10_COLORS[ 4 ] ));
			} else {
				Memory memory = (Memory)listCpu.get(0);
				listDataItem.add(new DataItem( memory.getMemfree(), String.format("Memfree (%s)", memory.getMemfree()), ColorsSWTUtils.CAT10_COLORS[ 0 ] ));
				listDataItem.add(new DataItem( memory.getBuffers(),	String.format("Buffers (%s)", memory.getBuffers()), ColorsSWTUtils.CAT10_COLORS[ 1 ] ));
				listDataItem.add(new DataItem( memory.getCached(), 	String.format("Cached (%s)", memory.getCached()), ColorsSWTUtils.CAT10_COLORS[ 2 ] ));
				listDataItem.add(new DataItem( memory.getSwap(),	String.format("Swap (%s)", memory.getSwap()), ColorsSWTUtils.CAT10_COLORS[ 3 ] ));
				listDataItem.add(new DataItem( memory.getDirty(),	String.format("Dirty (%s)", memory.getDirty()), ColorsSWTUtils.CAT10_COLORS[ 4 ] ));
			}
			
			final Display display = grpCpu.getDisplay();
		    display.asyncExec( new Runnable() {
		    	public void run() {
		    		pieChart.setItems(listDataItem.toArray(new DataItem[]{}));
		    	}
		    } );
			
		} catch(Exception e) {
			logger.error("initialize data", e);
		}
	}
	
	/**
	 * get cpu data
	 * @return
	 * @throws Exception
	 */
	public List<?> getUIData() throws Exception {
		return AgensManagerSQLImpl.getMemoryInfo(userDB, instance);
	}
}
