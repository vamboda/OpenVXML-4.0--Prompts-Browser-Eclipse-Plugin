package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.vtp.desktop.model.core.IDesignDocument;
import org.eclipse.vtp.desktop.model.core.IWorkflowProject;
import org.eclipse.vtp.desktop.model.core.design.IDesign;
import org.eclipse.vtp.desktop.views.promptsbrowser.views.*;

public class MediaSettingsContentProvider  implements IStructuredContentProvider {
	Utilities utilities=new Utilities();

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) 
	{
		if(inputElement instanceof IWorkflowProject)
			return utilities.processSettings((IWorkflowProject)inputElement);
		else if(inputElement instanceof IDesignDocument)
			return utilities.processSettings((IDesignDocument)inputElement);
		else
			return utilities.processSettings((IDesign)inputElement);
	}

}
