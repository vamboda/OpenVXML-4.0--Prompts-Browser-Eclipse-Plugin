package org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.core.design.IDesignElement;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.BrandBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.GenericBindingManager;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.InteractionBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.LanguageBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.NamedBinding;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.PropertyBindingItem;

public class PropertyProcessor  implements IPropertyProcessor{
	
	NamedBinding namedBinding = null;
	List<IBrand> brandsList = new ArrayList<IBrand>();
	
	public PropertyProcessor(List<IBrand> brandsList) {
		this.brandsList=brandsList;
	}
	
	
	public HashMap<String, String> process(String property,IDesignElement element)
	{
		HashMap<String,String> settingsHashMap = new HashMap<String,String>();
		GenericBindingManager configurationManagers = (GenericBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.generic");
		InteractionBinding interactionBinding = configurationManagers.getInteractionBinding("org.eclipse.vtp.framework.interactions.voice.interaction");
		namedBinding= interactionBinding.getNamedBinding(property);
		LanguageBinding languageBinding = namedBinding.getLanguageBinding("");
		if (brandsList != null && !brandsList.isEmpty()) {
			for (int b = 0; b < brandsList.size(); b++) {
				IBrand brand = ((IBrand) brandsList.get(b));
				BrandBinding brandBinding = languageBinding.getBrandBinding(brand);
				if (brandBinding != null) {
					PropertyBindingItem propertyBindingItem = (PropertyBindingItem) brandBinding.getBindingItem();
					if (propertyBindingItem != null) {
						String value = propertyBindingItem.getValue();
						if (value != null) {
							
							if(element.getName().equals("Record2"))
								System.out.println("DEBUD RRR:\t"+value+" "+brandBinding.getBrand().getName());
							settingsHashMap.put(brand.getName(), value);
							
						}
						else
						{
							do
							{
								brand=brand.getParent();
								brandBinding=languageBinding.getBrandBinding(brand);
								if (brandBinding != null) {
									propertyBindingItem = (PropertyBindingItem) brandBinding.getBindingItem();
									if(propertyBindingItem!=null)
									{
										value=propertyBindingItem.getValue();
										if(value!=null)
										{
											settingsHashMap.put(brand.getName(), value);
										}
									}
								}
							}while(value!=null);
						}
					}
				}
			}
		}
		
		return settingsHashMap;
	}
	
	public String process(String property,IDesignElement element,IBrand brand)
	{
		
		GenericBindingManager configurationManagers = (GenericBindingManager) element.getConfigurationManager("org.eclipse.vtp.configuration.generic");
		InteractionBinding interactionBinding = configurationManagers.getInteractionBinding("org.eclipse.vtp.framework.interactions.voice.interaction");
		namedBinding= interactionBinding.getNamedBinding(property);
		LanguageBinding languageBinding = namedBinding.getLanguageBinding("");
		BrandBinding brandBinding = languageBinding.getBrandBinding(brand);
		if (brandBinding != null)
		{
			PropertyBindingItem propertyBindingItem = (PropertyBindingItem) brandBinding.getBindingItem();
			if (propertyBindingItem != null) 
			{
				String value = propertyBindingItem.getValue();
				if (value != null)
					return value;
				else
					do
					{
						brand=brand.getParent();
						brandBinding=languageBinding.getBrandBinding(brand);
						if (brandBinding != null) 
						{
							propertyBindingItem = (PropertyBindingItem) brandBinding.getBindingItem();
							if(propertyBindingItem!=null)
							{
								value=propertyBindingItem.getValue();
								if(value!=null)
								{
									return value;
								}
							}
						}
					}while(value!=null);
				
			}
		
}
		
		return null;//Use default value
	}
	
	

}
