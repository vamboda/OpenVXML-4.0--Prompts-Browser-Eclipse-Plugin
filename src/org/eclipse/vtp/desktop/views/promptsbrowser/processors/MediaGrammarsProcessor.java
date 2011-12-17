package org.eclipse.vtp.desktop.views.promptsbrowser.processors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.model.interactive.core.IInteractiveWorkflowProject;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaFilesFolder;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaProject;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaProviderManager;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.BrandBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.GenericBindingManager;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.GrammarBindingItem;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.InteractionBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.LanguageBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.NamedBinding;
import org.eclipse.vtp.desktop.model.interactive.core.internal.MenuChoice;
import org.eclipse.vtp.desktop.model.interactive.core.mediadefaults.IMediaDefaultSettings;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties.IPropertyProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties.PropertyProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.DisplayElement;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaGrammarsElementRecord;
import org.eclipse.vtp.framework.interactions.core.media.CustomInputGrammar;
import org.eclipse.vtp.framework.interactions.core.media.FileInputGrammar;
import org.eclipse.vtp.framework.interactions.core.media.InputGrammar;
import org.eclipse.vtp.modules.interactive.ui.properties.MenuChoiceBindingManager;

public class MediaGrammarsProcessor implements IElementProcessor {
	private List<IBrand> brandsList;
	private List<String> languagesList = new ArrayList<String>();
	IInteractiveWorkflowProject project;
	IMediaDefaultSettings mediaDefaultSettings;
	IPropertyProcessor propertyProcessor;

	public MediaGrammarsProcessor(List<String> supportedLanguagesList,
			List<IBrand> supportedBrandsList) {
		this.languagesList = supportedLanguagesList;
		this.brandsList = supportedBrandsList;
		propertyProcessor = new PropertyProcessor(supportedBrandsList);
	}

	@Override
	public DisplayElement processElement(IDesignElement element) {
		boolean elementDisplayRow = true;
		boolean languageDisplayRow = true;
		boolean brandDisplayRow=true;
		DisplayElement displayElement = new DisplayElement();
		project = (IInteractiveWorkflowProject) element.getDesign().getDocument().getProject();
		mediaDefaultSettings = project.getMediaDefaultSettings();
		GenericBindingManager configurationManager = (GenericBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.generic");
		InteractionBinding interactionBinding = configurationManager.getInteractionBinding("org.eclipse.vtp.framework.interactions.voice.interaction");
		String property = (String) element.getProperties().get("type");
		if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.question"))) 
		{
			String defaultInputMode = mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.question","input-mode").getValue();
			for (String language : languagesList) 
			{
				languageDisplayRow = true;
				for (IBrand brand : brandsList) 
				{
					String brandName = brand.getName();
					String inputMode = propertyProcessor.process("input-mode", element, brand);
					if (inputMode == null) inputMode = defaultInputMode;
					MediaGrammarsElementRecord elementRecord=new MediaGrammarsElementRecord();
					if(elementDisplayRow)	elementRecord.elementNameString=element.getName();
					if(languageDisplayRow)	elementRecord.languageString=language ;
					languageDisplayRow=false;
					elementRecord.elementNameSeachTextString=element.getName();
					elementDisplayRow=false;
					elementRecord.brandString=brandName;
					if(brandDisplayRow)elementRecord.inputMode=inputMode;
					
					elementRecord.elementTypeString="question";
					if (inputMode.startsWith("D")) 
					{
						NameBindingProcessor nameBindingProcessorDTMF=new NameBindingProcessor(interactionBinding.getNamedBinding("Grammar"), language, brand);
						elementRecord.dtmfGrammarInputData=(nameBindingProcessorDTMF.inputGrammarData==null )? "Not Configured":nameBindingProcessorDTMF.inputGrammarData;
						if(!elementRecord.dtmfGrammarInputData.equalsIgnoreCase("not configured"))
						{
							if(nameBindingProcessorDTMF.isFileInputGrammar)
							{
								if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language)!=null)
								{
									IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language);
									IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
									if(mediaFilesFolder.getUnderlyingFolder().findMember(nameBindingProcessorDTMF.inputGrammarData)==null)
										elementRecord.fileExitsString="F";
									else
										elementRecord.fileExitsString="T";
								}
								else
									elementRecord.fileExitsString="F";
							}
						}
					}
					else if (inputMode.startsWith("Voice")) 
					{
						NameBindingProcessor nameBindingProcessorVoice=new NameBindingProcessor(interactionBinding.getNamedBinding("Voice-Grammar"), language, brand);
						elementRecord.voiceGrammarInputData=(nameBindingProcessorVoice.inputGrammarData==null )?"Not Configured":nameBindingProcessorVoice.inputGrammarData;
						if(!elementRecord.voiceGrammarInputData.equalsIgnoreCase("not configured"))
						{
							if(nameBindingProcessorVoice.isFileInputGrammar)
							{
								if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language)!=null)
								{
									IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language);
									IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
									if(mediaFilesFolder.getUnderlyingFolder().findMember(nameBindingProcessorVoice.inputGrammarData)==null)
										elementRecord.fileExitsString="-F";
									else
										elementRecord.fileExitsString="-T";
								}
								else
									elementRecord.fileExitsString="-F";
								
							}
						}
					}
					else if (inputMode.startsWith("Hybrid")) 
					{
						NameBindingProcessor nameBindingProcessorDTMF=new NameBindingProcessor(interactionBinding.getNamedBinding("Grammar"), language, brand);
						NameBindingProcessor nameBindingProcessorVoice=new NameBindingProcessor(interactionBinding.getNamedBinding("Voice-Grammar"), language, brand);
						elementRecord.dtmfGrammarInputData=(nameBindingProcessorDTMF.inputGrammarData==null )? "Not Configured":nameBindingProcessorDTMF.inputGrammarData;
						elementRecord.voiceGrammarInputData=(nameBindingProcessorVoice.inputGrammarData==null )?"Not Configured":nameBindingProcessorVoice.inputGrammarData;
						if(!elementRecord.dtmfGrammarInputData.equalsIgnoreCase("not configured"))
						{
							if(nameBindingProcessorDTMF.isFileInputGrammar)
							{
								if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language)!=null)
								{
									IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language);
									IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
									if(mediaFilesFolder.getUnderlyingFolder().findMember(nameBindingProcessorDTMF.inputGrammarData)==null )
										elementRecord.fileExitsString="F";
									else
										elementRecord.fileExitsString="T";
								}
								else
									elementRecord.fileExitsString="F";
							}
						}
						
						if(!elementRecord.voiceGrammarInputData.equalsIgnoreCase("not configured"))
						{
							if(nameBindingProcessorVoice.isFileInputGrammar)
							{
								if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language)!=null)
								{
									IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language);
									IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
									if(mediaFilesFolder.getUnderlyingFolder().findMember(nameBindingProcessorVoice.inputGrammarData)==null)
									{
										if(elementRecord.fileExitsString==null)
											elementRecord.fileExitsString="-F";
										else 
											elementRecord.fileExitsString+="-F";
									}
									else
									{
										if(elementRecord.fileExitsString==null)
											elementRecord.fileExitsString="-T";
										else 
											elementRecord.fileExitsString+="-T";
									}
								}
								else
								{
									if(elementRecord.fileExitsString==null)
										elementRecord.fileExitsString="-F";
									else 
										elementRecord.fileExitsString+="-F";}
							}
						}
						
					}
					displayElement.elementRecordList.add(elementRecord);
				}
			}
		}
		else if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.optionSet"))) 
		{
			String defaultInputMode = mediaDefaultSettings.getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction","org.eclipse.vtp.modules.interactive.optionSet","input-mode").getValue();
			MenuChoiceBindingManager menuChoiceBindingManager = (MenuChoiceBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.menuchoice");
			for (String language : languagesList)
			{
				languageDisplayRow = true;
				for (IBrand brand:brandsList) 
				{
					brandDisplayRow=true;
					String brandName = brand.getName();
					String inputMode = propertyProcessor.process("input-mode", element, brand);
					if (inputMode == null) inputMode = defaultInputMode;
					List<MenuChoice> menuChoices = menuChoiceBindingManager.getChoicesByBrand(brand);
					if(menuChoices.size()==0)
					{
						MediaGrammarsElementRecord elementRecord=new MediaGrammarsElementRecord();
						if(elementDisplayRow)	elementRecord.elementNameString=element.getName();
						elementDisplayRow=false;
						if(languageDisplayRow)	elementRecord.languageString=language ;
						languageDisplayRow=false;
						elementRecord.elementNameSeachTextString=element.getName();
						if(brandDisplayRow)elementRecord.brandString=brandName;
						if(brandDisplayRow)elementRecord.inputMode=inputMode;
						brandDisplayRow=false;
						elementRecord.elementTypeString="optionSet";
						displayElement.elementRecordList.add(elementRecord);
					}
					else
					{
						for (MenuChoice menuChoice : menuChoices) 
						{	
							MediaGrammarsElementRecord elementRecord=new MediaGrammarsElementRecord();
							if(elementDisplayRow)	elementRecord.elementNameString=element.getName();
							elementDisplayRow=false;
							if(languageDisplayRow)	elementRecord.languageString=language ;
							languageDisplayRow=false;
							elementRecord.elementNameSeachTextString=element.getName();
							if(brandDisplayRow)elementRecord.brandString=brandName;
							if(brandDisplayRow)elementRecord.inputMode=inputMode;
							brandDisplayRow=false;
							elementRecord.menuChoiceString=menuChoice.getOptionName();
							elementRecord.elementTypeString="optionSet";
							if (inputMode.startsWith("Voice")||inputMode.startsWith("Hybrid")) 
							{
								NameBindingProcessor nameBindingProcessorVoice=new NameBindingProcessor(interactionBinding.getNamedBinding(menuChoice.getOptionName() + "-grammar"), language, brand);
								elementRecord.voiceGrammarInputData=(nameBindingProcessorVoice.inputGrammarData==null)?"Not Configured":nameBindingProcessorVoice.inputGrammarData;
								if(!elementRecord.voiceGrammarInputData.equalsIgnoreCase("not configured"))
								{
									if(nameBindingProcessorVoice.isFileInputGrammar)
									{
										if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language)!=null)
										{
											IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brand, language);
											IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
											if(mediaFilesFolder.getUnderlyingFolder().findMember(nameBindingProcessorVoice.inputGrammarData)==null)
												elementRecord.fileExitsString="-F";
											else
												elementRecord.fileExitsString="-T";
										}
										else
											elementRecord.fileExitsString="-F";
									}
								}
								displayElement.elementRecordList.add(elementRecord);
							}
						}
					}
					
				}
			}
		}
		return displayElement;
	}
	
    private class NameBindingProcessor
    {
		String inputGrammarData;
		boolean isFileInputGrammar=false;

		public NameBindingProcessor(NamedBinding namedBinding,String language,IBrand brand) 
    	{
			LanguageBinding languageBinding = namedBinding.getLanguageBinding(language);
			BrandBinding brandBinding = languageBinding.getBrandBinding(brand);
			GrammarBindingItem grammarBindingItem = (GrammarBindingItem) brandBinding.getBindingItem();
			if (grammarBindingItem != null) 
			{
				InputGrammar inputGrammar = grammarBindingItem.getGrammar();
				if (inputGrammar instanceof CustomInputGrammar)		inputGrammarData=((CustomInputGrammar) inputGrammar).getCustomInputData();
				else if (inputGrammar instanceof FileInputGrammar)
				{
					
					inputGrammarData=((FileInputGrammar) inputGrammar).getPath();
					isFileInputGrammar=true;
					
				}
			} 
    	}
    }
}
