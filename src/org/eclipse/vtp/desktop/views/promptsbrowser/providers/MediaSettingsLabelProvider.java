package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vtp.desktop.views.promptsbrowser.Activator;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaGrammarsElementRecord;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaSettingsElementRecord;

public class MediaSettingsLabelProvider extends BaseLabelProvider implements
		ITableLabelProvider {
	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(15);

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ImageDescriptor descriptor=null;
		MediaSettingsElementRecord er=(MediaSettingsElementRecord)element;
		if(columnIndex==0)
		{
			if(er.elementTypeString.startsWith("play") && er.elementNameString!=null)
				descriptor = Activator.getImageDescriptor("icons/playprompt.png");
			else if(er.elementTypeString.startsWith("ques") && er.elementNameString!=null)
				descriptor = Activator.getImageDescriptor("icons/question.gif");
			else if(er.elementTypeString.startsWith("rec") && er.elementNameString!=null)
				descriptor = Activator.getImageDescriptor("icons/record.png");
			else if(er.elementTypeString.startsWith("opt") && er.elementNameString!=null)
				descriptor = Activator.getImageDescriptor("icons/optionset.png");
		}
		
		if(columnIndex==2)
		{
			if(er.variableString!= null && er.variableString.equalsIgnoreCase("Not Configured"))
				descriptor = Activator.getImageDescriptor("icons/error_tsk.gif");
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
	public String getColumnText(Object element, int columnIndex) 
	{
		MediaSettingsElementRecord elementRecord=(MediaSettingsElementRecord)element;
		
		String value=new String("DATA");
		if(columnIndex==0)
			value=elementRecord.elementNameString;
		if(columnIndex==1)
			value=elementRecord.brandString;
		if(columnIndex==2)
			if(elementRecord.variableString==null)value=""; else value=elementRecord.variableString;
		if(columnIndex==3)
			if(elementRecord.menuChoiceString==null)value=""; else value=elementRecord.menuChoiceString;
		if(columnIndex==4)
			if(elementRecord.inputModeString==null)value=""; else value=elementRecord.inputModeString;
		if(columnIndex==5)
			value=elementRecord.bargeString;
		if(columnIndex==6)
			if(elementRecord.playBeepString==null)value=""; else value=elementRecord.playBeepString;
		if(columnIndex==7)
			if(elementRecord.allowDTMFTermString==null)value=""; else value=elementRecord.allowDTMFTermString;
		if(columnIndex==8)
			if(elementRecord.dTMFValueString==null)value=""; else value=elementRecord.dTMFValueString;
		if(columnIndex==9)
			if(elementRecord.optionSilentString==null)value=""; else value=elementRecord.optionSilentString;
		if(columnIndex==10)
			if(elementRecord.initialInputTimeoutString==null)value=""; else value=elementRecord.initialInputTimeoutString;
		if(columnIndex==11)
			if(elementRecord.interdigitTimeoutString==null)value=""; else value=elementRecord.interdigitTimeoutString;
		if(columnIndex==12)
			if(elementRecord.terminationTimeoutString==null)value=""; else value=elementRecord.terminationTimeoutString;
		if(columnIndex==13)
			if(elementRecord.terminationCharacterString==null)value=""; else value=elementRecord.terminationCharacterString;
		if(columnIndex==14)
			if(elementRecord.speechIncompletionTimeoutString==null)value=""; else value=elementRecord.speechIncompletionTimeoutString;
		if(columnIndex==15)
			if(elementRecord.speechCompletionTimeoutString==null)value=""; else value=elementRecord.speechCompletionTimeoutString;
		if(columnIndex==16)
			if(elementRecord.maxSpeechLengthString==null)value=""; else value=elementRecord.maxSpeechLengthString;
		if(columnIndex==17)
			if(elementRecord.confidenceString==null)value=""; else value=elementRecord.confidenceString;
		if(columnIndex==18)
			if(elementRecord.sensitivityString==null)value=""; else value=elementRecord.sensitivityString;
		if(columnIndex==19)
			if(elementRecord.speedvsAccuracyString==null)value=""; else value=elementRecord.speedvsAccuracyString;
		if(columnIndex==20)
			if(elementRecord.maxRecordingTimeString==null)value=""; else value=elementRecord.maxRecordingTimeString;
		
		return value;
	}

}
