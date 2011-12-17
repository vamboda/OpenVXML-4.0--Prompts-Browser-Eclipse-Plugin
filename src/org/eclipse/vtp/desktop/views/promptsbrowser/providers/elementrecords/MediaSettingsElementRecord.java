package org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords;

public class MediaSettingsElementRecord implements IElementRecord 
{
	
	public String elementNameString;
	public String elementNameSeachTextString;
	public String elementTypeString;
	public String brandString;
	public String variableString;
	public String inputModeString;
	public String bargeString;
	public String playBeepString;
	public String menuChoiceString;
	public String dTMFValueString;
	public String optionSilentString;
	public String allowDTMFTermString;
	public String initialInputTimeoutString;
	public String interdigitTimeoutString;
	public String terminationTimeoutString;
	public String terminationCharacterString;
	public String speechIncompletionTimeoutString;
	public String speechCompletionTimeoutString;
	public String maxSpeechLengthString;
	public String confidenceString;
	public String sensitivityString;
	public String speedvsAccuracyString;
	public String maxRecordingTimeString;
	@Override
	public String getElementName() {
		
		return elementNameSeachTextString;
	}
}
