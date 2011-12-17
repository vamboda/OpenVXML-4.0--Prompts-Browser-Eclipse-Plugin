package org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties;

import java.util.HashMap;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;

public interface IPropertyProcessor 
{
	public HashMap<String,String> process(String property,IDesignElement element);
	public String process(String property,IDesignElement element,IBrand brand);
}
