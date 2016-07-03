package com.bitnine.angens.manager.core.editors.parts.lableprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bitnine.agens.manager.engine.core.dao.domain.custom.WALStatistics;

/**
 * WAL Statistics label provider
 * @author hangum
 *
 */
public class WALStatisticsTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		WALStatistics dao = (WALStatistics)element;

		switch (columnIndex) {
		case 0: return ""+dao.getWrite_total();
		case 1: return ""+dao.getWrite_speed();
		}
		
		return "****** not set column *********";
	}

}
