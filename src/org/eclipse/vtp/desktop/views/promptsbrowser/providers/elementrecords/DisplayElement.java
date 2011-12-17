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

import java.util.ArrayList;
import java.util.List;

public class DisplayElement
{

	public List<IElementRecord> elementRecordList = new ArrayList<IElementRecord>();

	public DisplayElement() {
		
	}

	public DisplayElement(List<IElementRecord> elementRecordList) {
		this.elementRecordList = elementRecordList;
	}

	public List<IElementRecord> getElementRecordList() {
		return this.elementRecordList;
	}

	public void addRecord(IElementRecord elementRecord) {
		
		this.elementRecordList.add(elementRecord);
	}

	public IElementRecord getRecord(int i) {
		return elementRecordList.get(i);
	}

	public int getRecordCount(List elementRecordList) {
		return elementRecordList.size();
	}

}
