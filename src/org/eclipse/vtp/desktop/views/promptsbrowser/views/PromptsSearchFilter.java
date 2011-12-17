package org.eclipse.vtp.desktop.views.promptsbrowser.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.internal.handlers.ReuseEditorTester;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.IElementRecord;

public class PromptsSearchFilter extends ViewerFilter {

	private String searchString;

	public void setSearchText(String s) {
		// Search must be a substring of the existing value
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) 
	{
		if (searchString == null || searchString.length() == 0) 
			return true;
		IElementRecord elementRecord= (IElementRecord) element;
		if(elementRecord.getElementName()!=null)
		{	
			if (elementRecord.getElementName().matches(searchString))	return true;
		}
		
		return false;
	}

	
}
