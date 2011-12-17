package org.eclipse.vtp.desktop.views.promptsbrowser.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.model.elements.core.internal.PrimitiveElement;
import org.eclipse.vtp.desktop.model.interactive.core.IInteractiveWorkflowProject;
import org.eclipse.vtp.desktop.model.interactive.core.internal.MenuChoice;
import org.eclipse.vtp.desktop.model.interactive.core.mediadefaults.IMediaDefaultSettings;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties.IPropertyProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties.PropertyProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.DisplayElement;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaGrammarsElementRecord;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaSettingsElementRecord;
import org.eclipse.vtp.desktop.views.promptsbrowser.views.Utilities;
import org.eclipse.vtp.modules.interactive.ui.QuestionInformationProvider;
import org.eclipse.vtp.modules.interactive.ui.RecordInformationProvider;
import org.eclipse.vtp.modules.interactive.ui.properties.MenuChoiceBindingManager;

public class MediaSettingsProcessor implements IElementProcessor
{
	IDesignElement designElement;
	IInteractiveWorkflowProject project;
	IMediaDefaultSettings mediaDefaultSettings;
	List<IBrand> brandsList = new ArrayList<IBrand>();
	public static final int PLAY_PROMPT = 0;
	public static final int QUESTION = 1;
	public static final int OPTIONSET = 2;
	public static final int RECORD = 3;
	IPropertyProcessor variablePropertyProcessor;
	IPropertyProcessor inputModePropertyProcessor;
	IPropertyProcessor bargePropertyProcessor;
	IPropertyProcessor playBeepPropertyProcessor;
	IPropertyProcessor allowDTMFTerminationPropertyProcessor;
	IPropertyProcessor initialInputTimeoutPropertyProcessor;
	IPropertyProcessor interdigitTimeoutPropertyProcessor;
	IPropertyProcessor terminationTimeoutPropertyProcessor;
	IPropertyProcessor terminationCharacterPropertyProcessor;
	IPropertyProcessor speechIncompletionTimeoutPropertyProcessor;
	IPropertyProcessor speechCompletionTimeoutPropertyProcessor;
	IPropertyProcessor maximumSpeechLengthPropertyProcessor;
	IPropertyProcessor confidenceLevelPropertyProcessor;
	IPropertyProcessor sensitivityLevelPropertyProcessor;
	IPropertyProcessor speedVsAccuracyPropertyProcessor;
	IPropertyProcessor maximumRecordingLengthPropertyProcessor;
	PropertyProcessor propertyProcessor;
	
	HashMap<String, String> defaultSettingsHashMap = new HashMap<String, String>();


	public MediaSettingsProcessor(List<IBrand> brandsList) 
	{
		this.brandsList=brandsList;
		propertyProcessor=new PropertyProcessor(brandsList);
		defaultSettingsHashMap.clear();
	}

	@Override
	public DisplayElement processElement(IDesignElement designElement) {
		this.designElement=designElement;
		DisplayElement displayElement = new DisplayElement();
		project = (IInteractiveWorkflowProject) designElement.getDesign().getDocument().getProject();
		mediaDefaultSettings=project.getMediaDefaultSettings();
		String property = (String) designElement.getProperties().get("type");
		if (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")) 
		{
			getDefaultSettings(PLAY_PROMPT);
			HashMap<String, String> bargePropertyHashMap = propertyProcessor.process("barge-in",designElement);
			boolean elementDisplayRow = true;
			for(IBrand brand:brandsList)
			{
				MediaSettingsElementRecord elementRecord = new MediaSettingsElementRecord();
				if(elementDisplayRow)	elementRecord.elementNameString=designElement.getName();
				elementRecord.brandString=brand.getName();
				elementRecord.bargeString=(bargePropertyHashMap.get(brand.getName())!=null)? bargePropertyHashMap.get(brand.getName()):defaultSettingsHashMap.get("bargeProperty");
				elementRecord.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
				elementRecord.elementNameSeachTextString=designElement.getName();
				displayElement.elementRecordList.add(elementRecord);
				elementDisplayRow=false;
			}
			bargePropertyHashMap.clear();
			defaultSettingsHashMap.clear();
		}
		else if (property.equals("org.eclipse.vtp.modules.interactive.question"))
		{
			getDefaultSettings(QUESTION);
			HashMap<String, String> inputModePropertyHashMap = propertyProcessor.process("input-mode",designElement);
			HashMap<String, String> bargePropertyHashMap = propertyProcessor.process("barge-in",designElement);
			HashMap<String, String> initialInputTimeoutPropertyHashMap= propertyProcessor.process("initial-timeout",designElement);
			HashMap<String, String> interdigitTimeoutPropertyHashMap= propertyProcessor.process("interdigit-timeout",designElement);
			HashMap<String, String> terminationTimeoutPropertyHashMap= propertyProcessor.process("termination-timeout",designElement);
			HashMap<String, String> terminationCharacterPropertyHashMap=propertyProcessor.process("termination-character",designElement);
			HashMap<String, String> speechIncompletionTimeoutPropertyHashMap=propertyProcessor.process("speech-incomplete-timeout",designElement);
			HashMap<String, String> speechCompletionTimeoutPropertyHashMap=propertyProcessor.process("speech-complete-timeout",designElement);
			HashMap<String, String> maximumSpeechLengthPropertyHashMap=propertyProcessor.process("max-speech-timeout",designElement);
			HashMap<String, String> confidenceLevelPropertyHashMap=propertyProcessor.process("confidence-level",designElement);
			HashMap<String, String> sensitivityLevelPropertyHashMap=propertyProcessor.process("sensitivity-level",designElement);
			HashMap<String, String> speedVsAccuracyPropertyHashMap=propertyProcessor.process("speed-vs-accuracy",designElement);
			boolean elementDisplayRow = true;
			for(IBrand brand:brandsList)
			{
				String brandName=brand.getName();
				MediaSettingsElementRecord elementRecord = new MediaSettingsElementRecord();
				if(elementDisplayRow)
					elementRecord.elementNameString=designElement.getName();
				elementRecord.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
				elementRecord.elementNameSeachTextString=designElement.getName();
				elementRecord.brandString=brandName;
				elementRecord.variableString=defaultSettingsHashMap.get("variableProperty");
				elementRecord.inputModeString=(inputModePropertyHashMap.get(brandName)!=null)?inputModePropertyHashMap.get(brandName):defaultSettingsHashMap.get("inputModeProperty");
				elementRecord.bargeString=(bargePropertyHashMap.get(brandName)!=null)? bargePropertyHashMap.get(brandName):defaultSettingsHashMap.get("bargeProperty");
				elementRecord.initialInputTimeoutString= (initialInputTimeoutPropertyHashMap.get(brandName)!=null) ? initialInputTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("initialInputTimeoutProperty");
				elementRecord.interdigitTimeoutString= (interdigitTimeoutPropertyHashMap.get(brandName)!=null) ? interdigitTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("interdigitTimeoutProperty");
				elementRecord.terminationTimeoutString = (terminationTimeoutPropertyHashMap.get(brandName)!=null) ? terminationTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("terminationTimeoutProperty");
				elementRecord.terminationCharacterString = (terminationCharacterPropertyHashMap.get(brandName)!=null) ? terminationCharacterPropertyHashMap.get(brandName):defaultSettingsHashMap.get("terminationCharacterProperty");
				elementRecord.speechIncompletionTimeoutString=(speechIncompletionTimeoutPropertyHashMap.get(brandName)!=null)?speechIncompletionTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speechIncompletionTimeoutProperty");
				elementRecord.speechCompletionTimeoutString=(speechCompletionTimeoutPropertyHashMap.get(brandName)!=null)?speechCompletionTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speechCompletionTimeoutProperty");
				elementRecord.maxSpeechLengthString=(maximumSpeechLengthPropertyHashMap.get(brandName)!=null)?maximumSpeechLengthPropertyHashMap.get(brandName):defaultSettingsHashMap.get("maximumSpeechLengthProperty");
				elementRecord.confidenceString=(confidenceLevelPropertyHashMap.get(brandName)!=null)?confidenceLevelPropertyHashMap.get(brandName):defaultSettingsHashMap.get("confidenceLevelProperty");
				elementRecord.sensitivityString=(sensitivityLevelPropertyHashMap.get(brandName)!=null)?sensitivityLevelPropertyHashMap.get(brandName):defaultSettingsHashMap.get("sensitivityLevelProperty");
				elementRecord.speedvsAccuracyString=(speedVsAccuracyPropertyHashMap.get(brandName)!=null)?speedVsAccuracyPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speedVsAccuracyProperty");
				displayElement.elementRecordList.add(elementRecord);
				elementDisplayRow=false;
			}
			defaultSettingsHashMap.clear();
			inputModePropertyHashMap.clear();
			bargePropertyHashMap.clear();
			initialInputTimeoutPropertyHashMap.clear();
			interdigitTimeoutPropertyHashMap.clear();
			terminationTimeoutPropertyHashMap.clear();
			terminationCharacterPropertyHashMap.clear();
			speechIncompletionTimeoutPropertyHashMap.clear();
			speechCompletionTimeoutPropertyHashMap.clear();
			maximumSpeechLengthPropertyHashMap.clear();
			confidenceLevelPropertyHashMap.clear();
			sensitivityLevelPropertyHashMap.clear();
			speedVsAccuracyPropertyHashMap.clear();
		} 
		else if (property.equals("org.eclipse.vtp.modules.interactive.optionSet")) 
		{
			getDefaultSettings(OPTIONSET);
			HashMap<String, String> inputModePropertyHashMap = propertyProcessor.process("input-mode",designElement);
			HashMap<String, String> bargePropertyHashMap = propertyProcessor.process("barge-in",designElement);
			HashMap<String, String> initialInputTimeoutPropertyHashMap= propertyProcessor.process("initial-timeout",designElement);
			HashMap<String, String> interdigitTimeoutPropertyHashMap= propertyProcessor.process("interdigit-timeout",designElement);
			HashMap<String, String> terminationTimeoutPropertyHashMap= propertyProcessor.process("termination-timeout",designElement);
			HashMap<String, String> speechIncompletionTimeoutPropertyHashMap=propertyProcessor.process("speech-incomplete-timeout",designElement);
			HashMap<String, String> speechCompletionTimeoutPropertyHashMap=propertyProcessor.process("speech-complete-timeout",designElement);
			HashMap<String, String> maximumSpeechLengthPropertyHashMap=propertyProcessor.process("max-speech-timeout",designElement);
			HashMap<String, String> confidenceLevelPropertyHashMap=propertyProcessor.process("confidence-level",designElement);
			HashMap<String, String> sensitivityLevelPropertyHashMap=propertyProcessor.process("sensitivity-level",designElement);
			HashMap<String, String> speedVsAccuracyPropertyHashMap=propertyProcessor.process("speed-vs-accuracy",designElement);
			MenuChoiceBindingManager menuChoiceBindingManager = (MenuChoiceBindingManager) designElement.getConfigurationManager("org.eclipse.vtp.configuration.menuchoice");
			boolean elementDisplayRow = true;
			
			for(IBrand brand:brandsList)
			{
				boolean brandDisplayRow=true;
				String brandName=brand.getName();
				List<MenuChoice>  menuChoices=menuChoiceBindingManager.getChoicesByBrand(brand);
				if(menuChoices.size()==0)
				{

					MediaSettingsElementRecord elementRecord = new MediaSettingsElementRecord();
					if(elementDisplayRow)	elementRecord.elementNameString=designElement.getName();
					elementRecord.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
					elementRecord.elementNameSeachTextString=designElement.getName();
					elementDisplayRow=false;
					if(brandDisplayRow)	elementRecord.brandString=brandName;
					brandDisplayRow=false;
					elementRecord.inputModeString=(inputModePropertyHashMap.get(brandName)!=null)?inputModePropertyHashMap.get(brandName):defaultSettingsHashMap.get("inputModeProperty");
					elementRecord.bargeString=(bargePropertyHashMap.get(brandName)!=null)? bargePropertyHashMap.get(brandName):defaultSettingsHashMap.get("bargeProperty");
					elementRecord.initialInputTimeoutString= (initialInputTimeoutPropertyHashMap.get(brandName)!=null) ? initialInputTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("initialInputTimeoutProperty");
					elementRecord.interdigitTimeoutString= (interdigitTimeoutPropertyHashMap.get(brandName)!=null) ? interdigitTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("interdigitTimeoutProperty");
					elementRecord.terminationTimeoutString = (terminationTimeoutPropertyHashMap.get(brandName)!=null) ? terminationTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("terminationTimeoutProperty");
					displayElement.elementRecordList.add(elementRecord);
				}
				else
				{
					for(MenuChoice menuChoice: menuChoices)
					{
						String tempDTMFValue=propertyProcessor.process(menuChoice.getOptionName()+"-dtmf", designElement,brand);
						String tempOptionSilentValue=propertyProcessor.process(menuChoice.getOptionName()+"-silent", designElement,brand);
						MediaSettingsElementRecord elementRecord = new MediaSettingsElementRecord();
						if(elementDisplayRow)	elementRecord.elementNameString=designElement.getName();
						elementRecord.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
						elementRecord.elementNameSeachTextString=designElement.getName();
						elementDisplayRow=false;
						if(brandDisplayRow)	elementRecord.brandString=brandName;
						brandDisplayRow=false;
						elementRecord.menuChoiceString=menuChoice.getOptionName();
						elementRecord.inputModeString=(inputModePropertyHashMap.get(brandName)!=null)?inputModePropertyHashMap.get(brandName):defaultSettingsHashMap.get("inputModeProperty");
						elementRecord.dTMFValueString=(tempDTMFValue!=null)?tempDTMFValue:"0";
						elementRecord.optionSilentString=(tempOptionSilentValue!=null)?tempOptionSilentValue:"false";
						elementRecord.bargeString=(bargePropertyHashMap.get(brandName)!=null)? bargePropertyHashMap.get(brandName):defaultSettingsHashMap.get("bargeProperty");
						elementRecord.initialInputTimeoutString= (initialInputTimeoutPropertyHashMap.get(brandName)!=null) ? initialInputTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("initialInputTimeoutProperty");
						elementRecord.interdigitTimeoutString= (interdigitTimeoutPropertyHashMap.get(brandName)!=null) ? interdigitTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("interdigitTimeoutProperty");
						elementRecord.terminationTimeoutString = (terminationTimeoutPropertyHashMap.get(brandName)!=null) ? terminationTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("terminationTimeoutProperty");
						elementRecord.speechIncompletionTimeoutString=(speechIncompletionTimeoutPropertyHashMap.get(brandName)!=null)?speechIncompletionTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speechIncompletionTimeoutProperty");
						elementRecord.speechCompletionTimeoutString=(speechCompletionTimeoutPropertyHashMap.get(brandName)!=null)?speechCompletionTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speechCompletionTimeoutProperty");
						elementRecord.maxSpeechLengthString=(maximumSpeechLengthPropertyHashMap.get(brandName)!=null)?maximumSpeechLengthPropertyHashMap.get(brandName):defaultSettingsHashMap.get("maximumSpeechLengthProperty");
						elementRecord.confidenceString=(confidenceLevelPropertyHashMap.get(brandName)!=null)?confidenceLevelPropertyHashMap.get(brandName):defaultSettingsHashMap.get("confidenceLevelProperty");
						elementRecord.sensitivityString=(sensitivityLevelPropertyHashMap.get(brandName)!=null)?sensitivityLevelPropertyHashMap.get(brandName):defaultSettingsHashMap.get("sensitivityLevelProperty");
						elementRecord.speedvsAccuracyString=(speedVsAccuracyPropertyHashMap.get(brandName)!=null)?speedVsAccuracyPropertyHashMap.get(brandName):defaultSettingsHashMap.get("speedVsAccuracyProperty");
						displayElement.elementRecordList.add(elementRecord);
					}	
				}
				
				brandDisplayRow=true;
			}
			
			defaultSettingsHashMap.clear();
			inputModePropertyHashMap.clear();
			bargePropertyHashMap.clear();
			initialInputTimeoutPropertyHashMap.clear();
			interdigitTimeoutPropertyHashMap.clear();
			terminationTimeoutPropertyHashMap.clear();
			speechIncompletionTimeoutPropertyHashMap.clear();
			speechCompletionTimeoutPropertyHashMap.clear();
			maximumSpeechLengthPropertyHashMap.clear();
			confidenceLevelPropertyHashMap.clear();
			sensitivityLevelPropertyHashMap.clear();
			speedVsAccuracyPropertyHashMap.clear();
		}
		else if (property.equals("org.eclipse.vtp.modules.interactive.record")) 
		{
			getDefaultSettings(RECORD);
			HashMap<String, String> bargePropertyHashMap = propertyProcessor.process("barge-in",designElement);
			HashMap<String, String> playBeepPropertyHashMap = propertyProcessor.process("play-beep",designElement);
			HashMap<String, String> allowDTMFTerminationPropertyHashMap = propertyProcessor.process("dtmf-termination",designElement);
			HashMap<String, String> initialInputTimeoutPropertyHashMap= propertyProcessor.process("initial-timeout",designElement);
			HashMap<String, String> terminationTimeoutPropertyHashMap= propertyProcessor.process("final-silence-timeout",designElement);
			HashMap<String, String> maximumRecordingLengthPropertyHashMap=propertyProcessor.process("max-record-time",designElement);
			boolean elementDisplayRow = true;
			for(IBrand brand:brandsList)
			{
				String brandName=brand.getName();
				MediaSettingsElementRecord elementRecord = new MediaSettingsElementRecord();
				if(elementDisplayRow)	elementRecord.elementNameString=designElement.getName();
				elementDisplayRow=false;
				elementRecord.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
				elementRecord.elementNameSeachTextString=designElement.getName();
				elementRecord.brandString=brandName;
				elementRecord.variableString=defaultSettingsHashMap.get("variableProperty");
				elementRecord.bargeString=(bargePropertyHashMap.get(brandName)!=null)? bargePropertyHashMap.get(brandName):defaultSettingsHashMap.get("bargeProperty");
				elementRecord.playBeepString= (playBeepPropertyHashMap.get(brandName)!=null) ? playBeepPropertyHashMap.get(brandName):defaultSettingsHashMap.get("playBeepProperty");
				elementRecord.allowDTMFTermString = (allowDTMFTerminationPropertyHashMap.get(brandName)!=null) ? allowDTMFTerminationPropertyHashMap.get(brandName):defaultSettingsHashMap.get("allowDTMFTerminationProperty");
				elementRecord.initialInputTimeoutString= (initialInputTimeoutPropertyHashMap.get(brandName)!=null) ? initialInputTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("initialInputTimeoutProperty");
				elementRecord.terminationTimeoutString=(terminationTimeoutPropertyHashMap.get(brandName)!=null) ? terminationTimeoutPropertyHashMap.get(brandName):defaultSettingsHashMap.get("terminationTimeoutProperty");
				elementRecord.maxRecordingTimeString=(maximumRecordingLengthPropertyHashMap.get(brandName)!=null) ? maximumRecordingLengthPropertyHashMap.get(brandName):defaultSettingsHashMap.get("maximumRecordingLengthProperty");

				displayElement.elementRecordList.add(elementRecord);
			}
			defaultSettingsHashMap.clear();
			bargePropertyHashMap.clear();
			playBeepPropertyHashMap.clear();
			allowDTMFTerminationPropertyHashMap.clear();
			initialInputTimeoutPropertyHashMap.clear();
			terminationTimeoutPropertyHashMap.clear();
			maximumRecordingLengthPropertyHashMap.clear();
		}
		return displayElement;
	}

	public void getDefaultSettings(int elementType) {

		switch (elementType) {
		case PLAY_PROMPT:
			defaultSettingsHashMap.put("bargeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.playPrompt","barge-in").getValue());
			break;
		case QUESTION:
			PrimitiveElement pe = (PrimitiveElement) this.designElement;
			QuestionInformationProvider questionInformationProvider = (QuestionInformationProvider) pe.getInformationProvider();
			if(questionInformationProvider.getVariableName().trim().length()==0)
				defaultSettingsHashMap.put("variableProperty","Not Configured");
			else
			defaultSettingsHashMap.put("variableProperty",questionInformationProvider.getVariableName());
			defaultSettingsHashMap.put("inputModeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","input-mode").getValue());
			defaultSettingsHashMap.put("bargeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","barge-in").getValue());
			defaultSettingsHashMap.put("initialInputTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","initial-timeout").getValue());
			defaultSettingsHashMap.put("interdigitTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","interdigit-timeout").getValue());
			defaultSettingsHashMap.put("terminationTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","termination-timeout").getValue());
			defaultSettingsHashMap.put("terminationCharacterProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","termination-character").getValue());
			defaultSettingsHashMap.put("speechIncompletionTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","speech-incomplete-timeout").getValue());
			defaultSettingsHashMap.put("speechCompletionTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","speech-complete-timeout").getValue());
			defaultSettingsHashMap.put("maximumSpeechLengthProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","max-speech-timeout").getValue());
			defaultSettingsHashMap.put("confidenceLevelProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","confidence-level").getValue());
			defaultSettingsHashMap.put("sensitivityLevelProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","sensitivity-level").getValue());
			defaultSettingsHashMap.put("speedVsAccuracyProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","speed-vs-accuracy").getValue());
			break;
		case OPTIONSET:
			defaultSettingsHashMap.put("inputModeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","input-mode").getValue());
			defaultSettingsHashMap.put("bargeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","barge-in").getValue());
			defaultSettingsHashMap.put("initialInputTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","initial-timeout").getValue());
			defaultSettingsHashMap.put("interdigitTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","interdigit-timeout").getValue());
			defaultSettingsHashMap.put("terminationTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","termination-timeout").getValue());
			defaultSettingsHashMap.put("speechIncompletionTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","speech-incomplete-timeout").getValue());
			defaultSettingsHashMap.put("speechCompletionTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","speech-complete-timeout").getValue());
			defaultSettingsHashMap.put("maximumSpeechLengthProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","max-speech-timeout").getValue());
			defaultSettingsHashMap.put("confidenceLevelProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","confidence-level").getValue());
			defaultSettingsHashMap.put("sensitivityLevelProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","sensitivity-level").getValue());
			defaultSettingsHashMap.put("speedVsAccuracyProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","speed-vs-accuracy").getValue());
			break;
		case RECORD:
			PrimitiveElement pe1 = (PrimitiveElement) this.designElement;
			RecordInformationProvider recordInformationProvider = (RecordInformationProvider) pe1.getInformationProvider();
			if(recordInformationProvider.getVariableName().trim().length()==0)
				defaultSettingsHashMap.put("variableProperty","Not Configured");
			else
				defaultSettingsHashMap.put("variableProperty",recordInformationProvider.getVariableName());
			defaultSettingsHashMap.put("bargeProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","barge-in").getValue());
			defaultSettingsHashMap.put("playBeepProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","play-beep").getValue());
			defaultSettingsHashMap.put("allowDTMFTerminationProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","dtmf-termination").getValue());
			defaultSettingsHashMap.put("initialInputTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","initial-timeout").getValue());
			defaultSettingsHashMap.put("terminationTimeoutProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","final-silence-timeout").getValue());
			defaultSettingsHashMap.put("maximumRecordingLengthProperty", mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.record","max-record-time").getValue());
			break;
		default:
			break;
		}

	}

}
