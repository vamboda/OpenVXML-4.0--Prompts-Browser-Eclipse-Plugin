package org.eclipse.vtp.desktop.views.promptsbrowser.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.vtp.desktop.model.core.IDesignDocument;
import org.eclipse.vtp.desktop.model.core.IWorkflowProject;
import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesign;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.model.interactive.core.IInteractiveWorkflowProject;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.IElementProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.MediaGrammarsProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.MediaPromptsProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.processors.MediaSettingsProcessor;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.DisplayElement;

public class Utilities {
	IElementProcessor elementProcessor = null;
	List<String> supportedInteractionsList = new ArrayList<String>();
	List<String> supportedLanguagesList = new ArrayList<String>();
	List<IBrand> supportedBrandsList = new ArrayList<IBrand>();
	public static final int WHITE=0;
	public static final int GRAY=1;
	public static final int BLACK=2;

	public Object[] processSettings(IWorkflowProject inputElement)
	{
		List elementRecordList = new ArrayList();
		if(inputElement instanceof IInteractiveWorkflowProject)
		{
			for(IDesignDocument document:inputElement.getDesignRootFolder().getDesignDocuments())
			{
				Object[] tempObjects=processSettings(document);
				for(Object tempObject:tempObjects)
					elementRecordList.add(tempObject);
			}
		}
		return elementRecordList.toArray();
	}

	public Object[] processSettings(IDesignDocument inputElement) 
	{
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		inputElement.becomeWorkingCopy();
		List<IDesignElement> designElements=inputElement.getMainDesign().getDesignElements();
		elementProcessor = new MediaSettingsProcessor(getSupportedBrands(inputElement.getProject()));
		for(IDesignElement designElement: designElements)
		{
			if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null) && (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")
				||property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet")
				||property.equals("org.eclipse.vtp.modules.interactive.record"))
				)
				{
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
			}
			else if(designElement.getType().equalsIgnoreCase("org.eclipse.vtp.desktop.model.elements.core.dialog"))
			{
				IDesignDocument designDocument=designElement.getDesign().getDocument();
				
				designDocument.becomeWorkingCopy();
				Object[] tempObjects=processSettings(designDocument.getDialogDesign(designElement.getId()));
				for(Object tempObject: tempObjects)
					elementRecordList.add(tempObject);
			}
		}
		return elementRecordList.toArray();
	}

	public Object[] processSettings(IDesign inputElement) 
	{
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		List<IDesignElement> designElements=inputElement.getDesignElements();
		elementProcessor = new MediaSettingsProcessor(getSupportedBrands(inputElement.getDocument().getProject()));
		for(IDesignElement designElement:designElements)
		{	if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")
				||property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet")
				||property.equals("org.eclipse.vtp.modules.interactive.record"))
				)
				{
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
			}
		}
		return elementRecordList.toArray();
	}

	
	public Object[] processPrompts(IWorkflowProject inputElement) 
	{
		List elementRecordList = new ArrayList();
		if(inputElement instanceof IInteractiveWorkflowProject)
		{	for(IDesignDocument document:inputElement.getDesignRootFolder().getDesignDocuments())
			{
				Object[] tempObjects=processPrompts(document);
				for(Object tempObject: tempObjects)
					elementRecordList.add(tempObject);
			}
		}
		return elementRecordList.toArray();
	}

	public Object[] processPrompts(IDesignDocument inputElement)
	{	
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		System.out.println("IS WORKING COPY\t:"+inputElement.getName()+":"+inputElement.isWorkingCopy());
		
		inputElement.becomeWorkingCopy();
		List<IDesignElement> designElements=inputElement.getMainDesign().getDesignElements();
		elementProcessor = new MediaPromptsProcessor(getSupportedLanguages(inputElement.getProject()),getSupportedBrands(inputElement.getProject()));
		for(IDesignElement designElement:designElements)
		{	
			if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")
				||property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet")
				||property.equals("org.eclipse.vtp.modules.interactive.record"))
				)
				{
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
			}
			else if(designElement.getType().equalsIgnoreCase("org.eclipse.vtp.desktop.model.elements.core.dialog"))
			{
				IDesignDocument designDocument=designElement.getDesign().getDocument();
				designDocument.becomeWorkingCopy();
				Object[] tempObjects=processPrompts(designDocument.getDialogDesign(designElement.getId()));
				for(Object tempObject:tempObjects)
					elementRecordList.add(tempObject);
			}
		}
		return elementRecordList.toArray();
	}

	public Object[] processPrompts(IDesign inputElement)
	{
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		List<IDesignElement> designElements=inputElement.getDesignElements();
		elementProcessor = new MediaPromptsProcessor(getSupportedLanguages(inputElement.getDocument().getProject()),getSupportedBrands(inputElement.getDocument().getProject()));
		for(IDesignElement designElement:designElements)
		{	if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null)&& (property.equals("org.eclipse.vtp.modules.interactive.playPrompt")
				||property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet")
				||property.equals("org.eclipse.vtp.modules.interactive.record"))
				)
				{
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
				
			}
		}
		
		return elementRecordList.toArray();
	}


	public Object[] processGrammars(IDesign inputElement)
	{
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		List<IDesignElement> designElements=inputElement.getDesignElements();
		elementProcessor = new MediaGrammarsProcessor(getSupportedLanguages(inputElement.getDocument().getProject()),getSupportedBrands(inputElement.getDocument().getProject()));
		for(int i=0;i<designElements.size();i++)
		{
			IDesignElement designElement=designElements.get(i);
			designElement.getType();
			if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null)&& property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet"))
				{
					
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
			}
		}
		return elementRecordList.toArray();
	}


	public Object[] processGrammars(IDesignDocument inputElement)
	{	
		DisplayElement displayElement = null;
		List elementRecordList = new ArrayList();
		inputElement.becomeWorkingCopy();
		List<IDesignElement> designElements=inputElement.getMainDesign().getDesignElements();
		elementProcessor = new MediaGrammarsProcessor(getSupportedLanguages(inputElement.getProject()),getSupportedBrands(inputElement.getProject()));
		for(int i=0;i<designElements.size();i++)
		{
			IDesignElement designElement=designElements.get(i);
			designElement.getType();
			if (designElement.getType().equals("org.eclipse.vtp.desktop.model.elements.core.basic"))
			{
				String property = (String) designElement.getProperties().get("type");
				if ((property != null) && property.equals("org.eclipse.vtp.modules.interactive.question")
				||property.equals("org.eclipse.vtp.modules.interactive.optionSet"))
				{
					displayElement = elementProcessor.processElement(designElement);
					elementRecordList.addAll(displayElement.getElementRecordList());
				}
			}
			else if(designElement.getType().equalsIgnoreCase("org.eclipse.vtp.desktop.model.elements.core.dialog"))
			{
				IDesignDocument designDocument=designElement.getDesign().getDocument();
				designDocument.becomeWorkingCopy();
				
				Object[] tempObjects=processGrammars(designDocument.getDialogDesign(designElement.getId()));
				for(int j=0;j<tempObjects.length;j++)
				{
					
					elementRecordList.add(tempObjects[j]);
				}
			}
		}
			return elementRecordList.toArray();
	}


	public Object[] processGrammars(IWorkflowProject inputElement) {
		List elementRecordList = new ArrayList();
		if(inputElement instanceof IInteractiveWorkflowProject)
		{
			for(IDesignDocument document:inputElement.getDesignRootFolder().getDesignDocuments())
			{
				Object[] tempObjects=processGrammars(document);
				for(int i=0;i<tempObjects.length;i++)
				{
					elementRecordList.add(tempObjects[i]);
				}
			}
		}
		return elementRecordList.toArray();
	}
	
	
	public static List<IBrand> getSupportedBrands(IWorkflowProject project)
	{
		IBrand defaultBrand=project.getBrandManager().getDefaultBrand();
		List<IBrand> supportedBrandsList=new ArrayList<IBrand>();
		HashMap<String,Integer> colorMap=new HashMap<String, Integer>();
		Vector< IBrand> queue=new Vector<IBrand>();
		queue.add(defaultBrand);
		supportedBrandsList.add(defaultBrand);
		colorMap.put(defaultBrand.getId(), GRAY);
		while(!queue.isEmpty())
		{
			IBrand brand=queue.remove(0);
			List<IBrand> childBrands= brand.getChildBrands();
			for(IBrand b:childBrands)
			{	
				if(colorMap.get(b.getId())==null)
				{
					System.out.println("BFS BRAND\t:"+b.getName());
					supportedBrandsList.add(b);
					colorMap.put(brand.getId(), GRAY);
					queue.add(b);
				}
				colorMap.put(brand.getId(),BLACK);
			}
		}
		
		return supportedBrandsList;
		
	}
	public static List<String> getSupportedLanguages(IWorkflowProject project)
	{
		List<String> supportedInteractionsList=((IInteractiveWorkflowProject) project).getSupportedInteractionTypes();
		List<String> supportedLanguagesList=new ArrayList<String>();
		for (String interaction:supportedInteractionsList) 
			supportedLanguagesList.addAll(((IInteractiveWorkflowProject) project).getSupportedLanguages(interaction));
		return supportedLanguagesList;
	}
	
}
