package com.bitnine.angens.manager.core.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.bitnine.angens.manager.core.Activator;
import com.bitnine.angens.manager.core.editors.AgensManagerEditor;
import com.bitnine.angens.manager.core.editors.AgensManagerEditorInput;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.swtdesigner.ResourceManager;

/**
 * open agens manager open
 * 
 * @author hangum
 *
 */
public class AgensManagerOpenAction extends Action implements ISelectionListener, IWorkbenchAction {
	private static final Logger logger = Logger.getLogger(AgensManagerOpenAction.class);
	private final static String ID = "com.bitnine.angens.manager.core.actions.AgensManagerOpenAction"; //$NON-NLS-1$
	protected final IWorkbenchWindow window;
	protected IStructuredSelection iss;
	protected UserDBDAO userDB;

	public AgensManagerOpenAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setToolTipText("Agens Manager");
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/agens_icons.png"));
		setEnabled(false);
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		AgensManagerEditorInput mei = new AgensManagerEditorInput(userDB);
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, AgensManagerEditor.ID);
		} catch (PartInitException e) {
			logger.error("open editor", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Manager open error", errStatus); //$NON-NLS-1$
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IStructuredSelection sel = (IStructuredSelection)selection;

		setEnabled(false);
		if(sel != null) {
			Object obj = sel.getFirstElement();
			if( obj instanceof UserDBDAO ) {
				userDB = (UserDBDAO)obj;
				isSelectEnable();
			} else if(obj instanceof UserDBResourceDAO) {
				UserDBResourceDAO userDBResource = (UserDBResourceDAO)obj;
				userDB = userDBResource.getParent();
				isSelectEnable();
			} else if(obj instanceof ResourcesDAO) {
				ResourcesDAO resourcesDAO = (ResourcesDAO)obj;
				userDB = resourcesDAO.getUserDBDAO();
				isSelectEnable();
			}
			
			if(userDB != null) {
				if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
					setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * is select button enable
	 */
	private void isSelectEnable() {
		if(TadpoleSecurityManager.getInstance().isLock(userDB)) {
			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {				
				setEnabled(true);
			}
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);		
	}

}
