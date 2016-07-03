package com.bitnine.angens.manager.core.editors.parts.lableprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bitnine.agens.manager.engine.core.dao.domain.Table;

/**
 * Disk Usageper Table label provider
 * @author hangum
 *
 */
public class DiskUsageperTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Table dao = (Table)element;
//		"database", "schema", "table", "size(MiB)", "table reads", "index reads", "total reads"
		switch (columnIndex) {
		case 0: return "";
		case 1: return "";
		case 2: return ""+dao.getName();
		case 3: return ""+dao.getSize();
//		case 4: return ""+dao.getRTotal();
//		case 5: return ""+dao.getAvail();
//		case 6: return ""+dao.getAvail();
		}
		
		return "****** not set column *********";
	}

}
