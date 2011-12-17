package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vtp.desktop.views.promptsbrowser.Activator;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaGrammarsElementRecord;

public class MediaGrammarsLabelProvider extends BaseLabelProvider implements
ITableLabelProvider{
	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(15);
	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		ImageDescriptor descriptor=null;
		MediaGrammarsElementRecord er=(MediaGrammarsElementRecord)element;
		if(columnIndex==0)
		{
			if(er.elementTypeString.startsWith("question") && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/question.gif");
			else if(er.elementTypeString.startsWith("optionSet")  && er.elementNameString.trim().length()!=0)
				descriptor =Activator.getImageDescriptor("icons/optionset.png");
		}
		
		if(columnIndex== 4 )
		{
			if(er.fileExitsString!=null)
			{	if(!er.fileExitsString.startsWith("-") && er.fileExitsString.charAt(0)=='F')
						descriptor =Activator.getImageDescriptor("icons/error_tsk.gif");
				else if(er.dtmfGrammarInputData!=null && er.dtmfGrammarInputData.equalsIgnoreCase("Not Configured"))
					descriptor =Activator.getImageDescriptor("icons/warning_tsk.gif");
			}
			else if(er.dtmfGrammarInputData!=null && er.dtmfGrammarInputData.equalsIgnoreCase("Not Configured"))
						descriptor =Activator.getImageDescriptor("icons/warning_tsk.gif");
		}
		else if(columnIndex== 5 )
		{
			if(er.fileExitsString!=null)
			{
				if(er.fileExitsString.contains("-"))
				{	if(er.fileExitsString.startsWith("-") && er.fileExitsString.charAt(1)=='F')
						descriptor =Activator.getImageDescriptor("icons/error_tsk.gif");
					else if(!er.fileExitsString.startsWith("-") && er.fileExitsString.charAt(2)=='F')
							descriptor =Activator.getImageDescriptor("icons/error_tsk.gif");
				}
				else if(er.voiceGrammarInputData!=null && er.voiceGrammarInputData.equalsIgnoreCase("Not Configured"))
					descriptor =Activator.getImageDescriptor("icons/warning_tsk.gif");
			}	else if(er.voiceGrammarInputData!=null && er.voiceGrammarInputData.equalsIgnoreCase("Not Configured"))
				descriptor =Activator.getImageDescriptor("icons/warning_tsk.gif");
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

		MediaGrammarsElementRecord er=(MediaGrammarsElementRecord)element;
		if (columnIndex == 0)
			return er.elementNameString;
		else if (columnIndex == 1)
			return er.languageString;
		else if (columnIndex == 2)
			return er.brandString;
		else if (columnIndex == 3)
			return (er.menuChoiceString!=null)? er.menuChoiceString : "";
		else if (columnIndex == 4)
		{
			if(er.dtmfGrammarInputData!=null)
			{
				if(er.dtmfGrammarInputData.startsWith("VXML:"))
					return er.dtmfGrammarInputData.substring(er.dtmfGrammarInputData.indexOf(":")+1);
				else
					return er.dtmfGrammarInputData;
			}
			return "";
		}
		else if(columnIndex==5)
		{
			if(er.voiceGrammarInputData!=null)
			{
				if(er.voiceGrammarInputData.startsWith("VXML:"))
					return er.voiceGrammarInputData.substring(er.voiceGrammarInputData.indexOf(":")+1);
				else
					return er.voiceGrammarInputData;
			}
			return "";
		}
		else if(columnIndex==6)
			return (er.inputMode!=null)? er.inputMode:"";
		return "DATA";
	
		
	}

}
