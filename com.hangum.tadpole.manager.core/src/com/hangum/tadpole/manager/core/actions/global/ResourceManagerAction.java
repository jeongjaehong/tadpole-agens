/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.actions.global;

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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.manager.core.editor.resource.ResourceManageEditor;
import com.hangum.tadpole.manager.core.editor.resource.ResourceManagerEditorInput;
import com.swtdesigner.ResourceManager;

/**
 * Resource manager action
 * 
 * @author hangum
 *
 */
public class ResourceManagerAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ResourceManagerAction.class);
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.ResourceManagerAction"; //$NON-NLS-1$
	
	private final IWorkbenchWindow window;
	private IStructuredSelection iss;
	
	public ResourceManagerAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().ResourceManagerAction_0);
		setToolTipText(Messages.get().ResourceManagerAction_0);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/resources.png")); //$NON-NLS-1$
		setEnabled(true);
	}
	
	@Override
	public void run() {
		try {
			ResourceManagerEditorInput userMe = new ResourceManagerEditorInput();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(userMe, ResourceManageEditor.ID);
		} catch (PartInitException e) {
			logger.error("Resource Management editor", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Resource Management editor", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}