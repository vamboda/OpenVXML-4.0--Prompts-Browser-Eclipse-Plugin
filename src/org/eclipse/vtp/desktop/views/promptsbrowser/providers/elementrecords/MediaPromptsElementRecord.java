package org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords;

/*--------------------------------------------------------------------------
 * Copyright (c) 2009 OpenMethods, LLC
 * All rights reserved. 
 * 
 * Initial API and implementation
 *	  Trip Gilman(OpenMethods)
 * Contributors:
 *    Vamshidhar Reddy Boda (OpenMethods)
 -------------------------------------------------------------------------*/
public class MediaPromptsElementRecord implements IElementRecord {

	public String elementNameString=new String();
	public String elementTypeString;
	public String elementNameSeachTextString;
	public String menuChoiceString;
	public String scriptTextString;
	public String languageString=new String();
	public String brandString=new String();
	public String contentTypeString;
	public String contentValueString;
	public String contentValueTypeString;
	public String inputModeString;
	public String fileExitsString;
	

	/*
	 * Valid Element Types: 1."PlayPrompt" 	2. "Question" 	3. "OptionSet" 	4. "Record"
	 */
	public void setElementType(String elementType) {
		this.elementTypeString = elementType;
	}

	public String getElementType() {
		return this.elementTypeString;
	}

	@Override
	public String getElementName() {
		return elementNameSeachTextString;
	}

}
