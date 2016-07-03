package com.bitnine.angens.manager.core.editors.parts.lableprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bitnine.agens.manager.engine.core.dao.domain.Tablespace;

/**
 * Disk Usageper Table Space label provider
 * @author hangum
 *
 */
public class DiskUsageperTableSpaceTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Tablespace dao = (Tablespace)element;

		switch (columnIndex) {
		case 0: return ""+dao.getName();
		case 1: return ""+dao.getLocation();
		case 2: return ""+dao.getDevice();
		case 3: return ""+dao.getTbs();
		case 4: return ""+dao.getTotal();
		case 5: return ""+dao.getAvail();
		}
		
		return "****** not set column *********";
	}

}
