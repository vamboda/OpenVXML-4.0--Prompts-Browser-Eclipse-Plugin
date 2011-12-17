package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.vtp.desktop.model.core.IDesignDocument;
import org.eclipse.vtp.desktop.model.core.IWorkflowProject;
import org.eclipse.vtp.desktop.model.core.design.IDesign;
import org.eclipse.vtp.desktop.views.promptsbrowser.Activator;

public class ProjectsTreeLabelProvider extends LabelProvider {

	String iconsPath = "icons/";
	private Map imageCache = new HashMap(11);
	

	public Image getImage(Object element) {
		ImageDescriptor descriptor=null;
		if (element instanceof IWorkflowProject)
			descriptor = Activator.getImageDescriptor("icons/folder_open.gif");

		else if (element instanceof IDesignDocument)
			descriptor =Activator.getImageDescriptor("icons/application.gif");
			
		else
			descriptor =Activator.getImageDescriptor("icons/icon_dialog.gif");
			
		Image image = null;
		if (descriptor != null) {
			image = (Image) imageCache.get(descriptor);
			if (image == null) {
				image = descriptor.createImage();
				imageCache.put(descriptor, image);
			}
		}

		return image;

		
	}

	public String getText(Object element) {
		if (element instanceof IWorkflowProject)
			return ((IWorkflowProject) element).getName();
		else if (element instanceof IDesignDocument) {
			String name = ((IDesignDocument) element).getName();
			int index = name.lastIndexOf(".canvas");
			return name.substring(0, index);
		} else {
			System.out.println("Debug:" + ((IDesign) element).getName());
			return ((IDesign) element).getName();
		}
	}

	public void dispose() {
		return;
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type "
				+ element.getClass().getName());
	}

}
