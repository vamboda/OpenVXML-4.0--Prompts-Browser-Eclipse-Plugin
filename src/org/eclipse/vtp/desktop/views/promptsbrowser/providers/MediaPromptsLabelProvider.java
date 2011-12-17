package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vtp.desktop.views.promptsbrowser.Activator;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaPromptsElementRecord;

public class MediaPromptsLabelProvider extends BaseLabelProvider implements
ITableLabelProvider{
	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(15);
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ImageDescriptor descriptor=null;
		MediaPromptsElementRecord er=(MediaPromptsElementRecord)element;
		if(columnIndex==0)
		{
			if(er.elementTypeString.startsWith("play") && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/playprompt.png");
			else if(er.elementTypeString.startsWith("ques") && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/question.gif");
			else if(er.elementTypeString.startsWith("rec") && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/record.png");
			else if(er.elementTypeString.startsWith("opt") && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/optionset.png");
		}
		if(columnIndex==4)
		{
			if(er.contentValueString!=null && er.contentValueString.equals("Not Configured"))
				descriptor =Activator.getImageDescriptor("icons/warning_tsk.gif");
			else
			{
				if(er.fileExitsString!=null && er.fileExitsString.equals("false"))
					descriptor =Activator.getImageDescriptor("icons/error_tsk.gif");
			}
		}
		Image image = null;
		if (descriptor != null) {
			image = imageCache.get(descriptor);
			if (image == null) {
				image = descriptor.createImage();
				imageCache.put(descriptor, image);
			}
		}
		return image;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		MediaPromptsElementRecord er=(MediaPromptsElementRecord)element;
		if (columnIndex == 0)
			return er.elementNameString;
		else if (columnIndex == 1)
			return er.languageString;
		else if (columnIndex == 2)
			return er.brandString;
		else if (columnIndex == 3)
			return (er.menuChoiceString!=null)? er.menuChoiceString : "";
		else if (columnIndex == 4)
			return er.contentValueString;
		
			
		else if(columnIndex==5)
			return (er.contentTypeString!=null)? er.contentTypeString+"("+er.contentValueTypeString.charAt(0)+")":"";
		else if (columnIndex == 6)
				return (er.scriptTextString!=null)? er.scriptTextString : "";
		return "[ERROR]";
	
		
	}

}
