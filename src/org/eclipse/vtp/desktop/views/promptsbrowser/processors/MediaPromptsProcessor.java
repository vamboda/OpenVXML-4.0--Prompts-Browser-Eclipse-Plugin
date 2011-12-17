package org.eclipse.vtp.desktop.views.promptsbrowser.processors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.model.core.internal.branding.Brand;
import org.eclipse.vtp.desktop.model.interactive.core.IInteractiveWorkflowProject;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaFilesFolder;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaProject;
import org.eclipse.vtp.desktop.model.interactive.core.IMediaProviderManager;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.BrandBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.GenericBindingManager;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.InteractionBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.LanguageBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.NamedBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.PromptBindingEntry;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.PromptBindingItem;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.PromptBindingNode;
import org.eclipse.vtp.desktop.model.interactive.core.internal.MenuChoice;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties.ProcessContent;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.DisplayElement;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaPromptsElementRecord;
import org.eclipse.vtp.framework.interactions.core.media.Content;
import org.eclipse.vtp.modules.interactive.ui.properties.MenuChoiceBindingManager;

public class MediaPromptsProcessor implements IElementProcessor
{
	private List<IBrand> brandsList;
	private List<String> languagesList = new ArrayList<String>();
	IInteractiveWorkflowProject project;


	public MediaPromptsProcessor(List<String> supportedLanguagesList,List<IBrand> supportedBrandsList) 
	{
		this.languagesList = supportedLanguagesList;
		this.brandsList = supportedBrandsList;
	}
	
	@Override
	public DisplayElement processElement(IDesignElement element) 
	{
		boolean elementDisplayRow = true;
		boolean languageDisplayRow = true;
		boolean brandDisplayRow = true;
		DisplayElement displayElement=new DisplayElement();
		project=(IInteractiveWorkflowProject)element.getDesign().getDocument().getProject();
		
		GenericBindingManager configurationManager = (GenericBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.generic");
		InteractionBinding interactionBinding = configurationManager.getInteractionBinding("org.eclipse.vtp.framework.interactions.voice.interaction");
		String property = (String) element.getProperties().get("type");
		if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")
		||property.equals("org.eclipse.vtp.modules.interactive.question")
		||property.equals("org.eclipse.vtp.modules.interactive.record")))
		{
			
			NamedBinding nameBindingPrompt = interactionBinding.getNamedBinding("Prompt");
			for(String language:languagesList)
			{
				languageDisplayRow = true;
				LanguageBinding languageBinding = nameBindingPrompt.getLanguageBinding(language);
				if(languageBinding!=null)
				for(int b=0;b<brandsList.size();b++)
				{	
					String brandName=brandsList.get(b).getName();
					brandDisplayRow=true;
					BrandBinding brandBinding = languageBinding.getBrandBinding(((Brand) brandsList.get(b)));
					if (brandBinding != null) 
					{
						PromptBindingItem promptBindingItem = (PromptBindingItem) brandBinding.getBindingItem();
						if (promptBindingItem != null) 
						{
							List<PromptBindingNode> promptBindingNodes = promptBindingItem.getEntries();
							if (promptBindingNodes != null&& promptBindingNodes.size() != 0) 
							{
								for (PromptBindingNode promptBindingNode:promptBindingNodes)
								{
									MediaPromptsElementRecord er = new MediaPromptsElementRecord();
									if (elementDisplayRow)	er.elementNameString=element.getName();
									elementDisplayRow = false;
									er.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
									er.elementNameSeachTextString=element.getName();
									if (languageDisplayRow)	er.languageString=language;
									languageDisplayRow = false;
									if(brandDisplayRow) er.brandString=brandName;
									brandDisplayRow=false;
									Content content = ((PromptBindingEntry)promptBindingNode).getContent();
									ProcessContent processedContent = new ProcessContent(content);
									er.contentTypeString=processedContent.contentType;
									er.contentValueString=processedContent.contentValue;
									if(er.contentTypeString.equalsIgnoreCase("audio") )
									{
										if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brandsList.get(b), language)!=null)
										{
											IMediaProject mediaProject=((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brandsList.get(b), language);
											IMediaFilesFolder mediaFilesFolder=mediaProject.getMediaFilesFolder();
											if(mediaFilesFolder.getUnderlyingFolder().findMember(er.contentValueString) == null)
												er.fileExitsString="false";
											else
												er.fileExitsString="true";
										}
										
									}
									er.contentValueTypeString=processedContent.contentValueType;
									displayElement.elementRecordList.add(er);
								}
							}
						}
						else
						{
							MediaPromptsElementRecord er = new MediaPromptsElementRecord();
							if (elementDisplayRow)	er.elementNameString=element.getName();
							elementDisplayRow = false;
							er.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
							er.contentValueString="Not Configured";
							er.elementNameSeachTextString=element.getName();
							if (languageDisplayRow)	er.languageString=language;
							languageDisplayRow = false;
							if(brandDisplayRow) er.brandString=brandName;
							brandDisplayRow=false;
							displayElement.elementRecordList.add(er);
						}
					}
				}
			}
		}
		
		else //Option Set process code
		{
			MenuChoiceBindingManager menuChoiceBindingManager = (MenuChoiceBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.menuchoice");
			boolean menuChoiceDisplayRow=true;
			NamedBinding nameBindingPrompt = interactionBinding.getNamedBinding("Prompt");
			for(String language:languagesList)
			{
				languageDisplayRow = true;
				LanguageBinding languageBinding = nameBindingPrompt.getLanguageBinding(language);
				if(languageBinding!=null)
				for(int b=0;b<brandsList.size();b++)
				{	
					String brandName=brandsList.get(b).getName();
					brandDisplayRow=true;
					BrandBinding brandBinding = languageBinding.getBrandBinding(((Brand) brandsList.get(b)));
					if (brandBinding != null) 
					{
						List<MenuChoice>  menuChoices=menuChoiceBindingManager.getChoicesByBrand(brandsList.get(b));
						if(menuChoices.size()==0)
						{

							MediaPromptsElementRecord er = new MediaPromptsElementRecord();
							if (elementDisplayRow)	er.elementNameString=element.getName();
							elementDisplayRow = false;
							er.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
							er.elementNameSeachTextString=element.getName();
							if (languageDisplayRow)	er.languageString=language;
							languageDisplayRow = false;
							if(brandDisplayRow) er.brandString=brandName;
							brandDisplayRow=false;
							displayElement.elementRecordList.add(er);
						
						}
						else
						{
							for(MenuChoice menuChoice:menuChoices)
							{	
								menuChoiceDisplayRow=true;
								NamedBinding nameBindingChoicePrompt = interactionBinding.getNamedBinding(menuChoice.getOptionName()+"-prompt");
								LanguageBinding languageBindingChoicePrompt = nameBindingChoicePrompt.getLanguageBinding(language);
								BrandBinding brandBindingChoicePrompt=languageBindingChoicePrompt.getBrandBinding(((Brand) brandsList.get(b)));
								PromptBindingItem promptBindingItem = (PromptBindingItem) brandBindingChoicePrompt.getBindingItem();
								if (promptBindingItem != null) 
								{
									List<? extends PromptBindingNode> promptBindingNodes = (List <?extends PromptBindingNode>)promptBindingItem.getEntries();
									if (promptBindingNodes != null&& promptBindingNodes.size() != 0) 
									{
										for (PromptBindingNode promptBindingNode:promptBindingNodes)
										{
											MediaPromptsElementRecord er = new MediaPromptsElementRecord();
											if (elementDisplayRow)	er.elementNameString=element.getName();
											elementDisplayRow = false;
											er.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
											er.elementNameSeachTextString=element.getName();
											if(menuChoiceDisplayRow)
											{
												er.menuChoiceString=menuChoice.getOptionName();
												er.scriptTextString=menuChoice.getScriptText();
											}
											menuChoiceDisplayRow=false;
											if (languageDisplayRow)	er.languageString=language;
											languageDisplayRow = false;
											if(brandDisplayRow) er.brandString=brandName;
											brandDisplayRow=false;
											Content content = ((PromptBindingEntry)promptBindingNode).getContent();
											ProcessContent processedContent = new ProcessContent(content);
											er.contentTypeString=processedContent.contentType;
											er.contentValueString=processedContent.contentValue;
											if(er.contentTypeString.equalsIgnoreCase("audio"))
											{
												if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brandsList.get(b), language)!=null)
												{
													if(((IMediaProviderManager)project).getMediaProject("org.eclipse.vtp.framework.interactions.voice.interaction", brandsList.get(b), language).getMediaFilesFolder().getUnderlyingFolder().findMember(er.contentValueString) == null)
														er.fileExitsString="false";
													else
														er.fileExitsString="true";
												}
												
											}
											er.contentValueTypeString=processedContent.contentValueType;
											displayElement.elementRecordList.add(er);
										}
									}
								}
								else
								{
									MediaPromptsElementRecord er = new MediaPromptsElementRecord();
									if (elementDisplayRow)	er.elementNameString=element.getName();
									elementDisplayRow = false;
									er.elementTypeString=property.substring(property.lastIndexOf(".") + 1);
									er.elementNameSeachTextString=element.getName();
									er.menuChoiceString=menuChoice.getOptionName();
									er.contentValueString="Not Configured";
									if (languageDisplayRow)	er.languageString=language;
									languageDisplayRow = false;
									if(brandDisplayRow) er.brandString=brandName;
									brandDisplayRow=false;
									displayElement.elementRecordList.add(er);
								}
							}
						}
						
					}
				}
			}
		
			
			
		}
		return displayElement;
	}
}