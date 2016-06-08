package com.bitnine.angens.manager.core.editors.parts.lableprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bitnine.agens.manager.engine.core.dao.domain.Database;

/**
 * Recovery conflicts label provider
 * @author hangum
 *
 */
public class RecoveryConflictsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Database dao = (Database)element;

		switch (columnIndex) {
		case 0: return ""+dao.getName();
		case 1: return ""+dao.getConfl_tablespace();
		case 2: return ""+dao.getConfl_lock();
		case 3: return ""+dao.getConfl_snapshot();
		case 4: return ""+dao.getConfl_bufferpin();
		case 5: return ""+dao.getConfl_deadlock();
		}
		
		return null;
	}

}
