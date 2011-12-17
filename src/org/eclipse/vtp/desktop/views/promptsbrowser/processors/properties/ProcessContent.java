package org.eclipse.vtp.desktop.views.promptsbrowser.processors.properties;

import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.interactive.core.configuration.generic.InteractionBinding;
import org.eclipse.vtp.framework.interactions.core.media.Content;
import org.eclipse.vtp.framework.interactions.core.media.FileContent;
import org.eclipse.vtp.framework.interactions.core.media.FormattableContent;
import org.eclipse.vtp.framework.interactions.core.media.TextContent;

public class ProcessContent {
	String filePath = "C:\\vamshi_eclipse\\MoneyPennyEn\\Media Files\\";
	public String languageString = new String();
	public IBrand brand;
	public InteractionBinding interactionBinding;
	public String contentType;
	public String contentValue;
	public String contentValueType;
	public String fileExists;

	public ProcessContent() {
	}

	public ProcessContent(Content content)
	{
		contentType = content.getContentType();
		if (contentType.equalsIgnoreCase("org.eclipse.vtp.framework.interactions.voice.media.content.audio")) 
		{
			contentValue =  ((FileContent) content).getPath();
			contentValueType=(((FileContent) content).getPathType()==FileContent.STATIC_PATH)?"static":"variable";
		}
		else if (contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.number")
				|| contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.digits")
				|| contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.money")
				|| contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.letters")
				|| contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.date")
				|| contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.ordinal"))
		{
			contentValue =  ((FormattableContent) content).getValue();
			contentValueType=(((FormattableContent) content).getValueType()==FormattableContent.STATIC_VALUE)?"static":"variable";
		}

		else if (contentType.equals("org.eclipse.vtp.framework.interactions.core.media.content.text"))
		{
			contentValue =  ((TextContent) content).getText();
			contentValueType=(((TextContent) content).getTextType()==TextContent.STATIC_TEXT)?"static":"variable";
		}
		contentType = contentType.substring(contentType.lastIndexOf(".") + 1);
	}

}