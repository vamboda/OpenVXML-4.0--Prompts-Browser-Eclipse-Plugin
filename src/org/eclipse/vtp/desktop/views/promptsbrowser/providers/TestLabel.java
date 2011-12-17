package org.eclipse.vtp.desktop.views.promptsbrowser.providers;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaPromptsElementRecord;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.elementrecords.MediaSettingsElementRecord;

public class TestLabel extends ColumnLabelProvider implements IStyledLabelProvider {
	private static int IMAGE_SIZE= 16;
	static Display display=Display.getDefault();
	private static final Image IMAGE1= new Image(display, display.getSystemImage(SWT.ICON_WARNING).getImageData().scaledTo(IMAGE_SIZE, IMAGE_SIZE));
	private static final Image IMAGE2= new Image(display, display.getSystemImage(SWT.ICON_ERROR).getImageData().scaledTo(IMAGE_SIZE, IMAGE_SIZE));

	public String getText(Object element) {
		MediaSettingsElementRecord er=(MediaSettingsElementRecord)element;
		return er.elementNameString;
	}
		public Image getImage(Object element) {
			
			return IMAGE1;
		}

	public StyledString getStyledText(Object element) 
	{
//		if (element instanceof File) {
//			
//		}
		System.out.println("ELEMEY TYPE:"+element);
		return null;
	}
}
