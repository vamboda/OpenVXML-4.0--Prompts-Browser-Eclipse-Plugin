package org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords;

public class MediaGrammarsElementRecord implements IElementRecord {

	public String elementNameString=new String();
	public String elementTypeString;
	public String elementNameSeachTextString;
	public String menuChoiceString;
	public String languageString=new String();
	public String brandString=new String();
	public String inputMode=new String();
	public String dtmfGrammarInputData;
	public String voiceGrammarInputData;
	//public String grammarValueTypeString;
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
