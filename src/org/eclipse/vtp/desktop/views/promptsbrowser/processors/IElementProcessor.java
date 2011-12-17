package org.eclipse.vtp.desktop.views.promptsbrowser.processors;

import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.DisplayElement;

public interface IElementProcessor 
{
	public DisplayElement processElement(IDesignElement element);

}
