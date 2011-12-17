package org.eclipse.vtp.desktop.views.promptsbrowser.providers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.vtp.desktop.model.core.IDesignDocument;
import org.eclipse.vtp.desktop.model.core.IWorkflowProject;
import org.eclipse.vtp.desktop.model.core.WorkflowCore;
import org.eclipse.vtp.desktop.model.core.branding.IBrand;
import org.eclipse.vtp.desktop.model.interactive.core.IInteractiveWorkflowProject;
import org.eclipse.vtp.desktop.model.interactive.core.internal.InteractiveWorkflowProject;
import org.eclipse.vtp.desktop.model.interactive.core.mediadefaults.IMediaDefaultSettings;
public class ProjectsTreeContentProvider implements ITreeContentProvider{
	public static Object[] EmptyArray = new Object[0];
	public static final int WHITE=0;
	public static final int GRAY=1;
	public static final int BLACK=2;
	String parentElement = new String("hi");
	@Override

	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
	//System.out.println("hallo");
	
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		System.out.println("TreeContentProvide: getElement");
		IProject projects[]=ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IWorkflowProject> workflowProjects=new ArrayList<IWorkflowProject>();
		for(int i=0;i<projects.length;i++)
		{
			IWorkflowProject workflowProject=WorkflowCore.getDefault().getWorkflowModel().convertToWorkflowProject(projects[i]);
			if(workflowProject instanceof InteractiveWorkflowProject)
			{	workflowProjects.add(workflowProject);
			IBrand defaultBrand = workflowProject.getBrandManager().getDefaultBrand();
			HashMap<String,Integer> colorMap=new HashMap<String, Integer>();
			Vector< IBrand> queue=new Vector<IBrand>();
			queue.add(defaultBrand);
			System.out.println("BFS BRAND\t:"+defaultBrand.getName());
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
						colorMap.put(brand.getId(), GRAY);
						queue.add(b);
					}
					colorMap.put(brand.getId(),BLACK);
				}
			}
				
			IInteractiveWorkflowProject iInteractiveWorkflowProject=(IInteractiveWorkflowProject)workflowProject;
			IMediaDefaultSettings iMediaDefaultSettings=iInteractiveWorkflowProject.getMediaDefaultSettings();
			//System.out.println("I am the value for "+"\""+workflowProject.getName() +"\""+": "+iInteractiveWorkflowProject.getMediaDefaultSettings().getDefaultSetting("org.eclipse.vtp.framework.interactions.voice.interaction",  "org.eclipse.vtp.modules.interactive.playPrompt", "barge-in").getValue());
			}
		}
		System.out.println("TreeContentProvide: exiting getElement---XXXX");
		return workflowProjects.toArray();
	}

	@Override
	public Object[] getChildren(Object element)
	{
		if(element instanceof IWorkflowProject)
		{
			return ((IWorkflowProject)element).getDesignRootFolder().getDesignDocuments().toArray();
		}
		else if(element instanceof IDesignDocument)
		{
			IDesignDocument designDocument=(IDesignDocument)element;
			designDocument.becomeWorkingCopy();
			Object[] objects= designDocument.getDialogDesigns().toArray();
			return objects;
			
		}
		
		
		return EmptyArray;
	}

	@Override
	public Object getParent(Object element) 
	{
		return parentElement;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
		}

}
