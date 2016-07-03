package com.bitnine.angens.manager.core.editors.parts.lableprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bitnine.agens.manager.engine.core.dao.domain.custom.InstanceProcessRation;

/**
 * WAL Statistics label provider
 * @author hangum
 *
 */
public class InstanceProcessRatioTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		InstanceProcessRation dao = (InstanceProcessRation)element;

		switch (columnIndex) {
		case 0: return ""+dao.getIdle();
		case 1: return ""+dao.getIdleInXact();
		case 2: return ""+dao.getWaiting();
		case 3: return ""+dao.getRunning();
		}
		
		return "****** not set column *********";
	}

}
