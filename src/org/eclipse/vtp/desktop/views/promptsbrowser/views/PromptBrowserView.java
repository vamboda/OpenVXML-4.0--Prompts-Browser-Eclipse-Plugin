package org.eclipse.vtp.desktop.views.promptsbrowser.views;



import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaGrammarsContentProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaGrammarsLabelProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaPromptsContentProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaPromptsLabelProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaSettingsContentProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.MediaSettingsLabelProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.ProjectsTreeContentProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.ProjectsTreeLabelProvider;
import org.eclipse.vtp.desktop.views.promptsbrowser.providers.TestLabel;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class PromptBrowserView extends ViewPart 
{
	
	TreeViewer projectTreeViewer=null;
	TableViewer settingsTableViewer=null;
	TableViewer promptsTableViewer=null;
	TableViewer grammarsTableViewer=null;
	CTabFolder customTabFolder;
	private Text searchText;
	private PromptsSearchFilter filter;


	public PromptBrowserView() {}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) 
	{
		GridLayout compositeGridLayout = new GridLayout(3, false);
		parent.setLayout(compositeGridLayout);
		GridData gridData=new GridData();
		final Tree projectTree = new Tree(parent,  SWT.BORDER);
		projectTreeViewer = new TreeViewer(projectTree);
		gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.verticalSpan = 2;
		gridData.widthHint = 245;
		projectTreeViewer.getTree().setLayoutData(gridData);
		projectTreeViewer.getTree().setHeaderVisible(true);
		TreeViewerColumn projectsViewerColumn = new TreeViewerColumn(projectTreeViewer, SWT.NONE);
		projectsViewerColumn.getColumn().setText("Projects");
		projectsViewerColumn.getColumn().setWidth(350);
		projectTreeViewer.setContentProvider(new ProjectsTreeContentProvider());
		projectTreeViewer.setLabelProvider(new ProjectsTreeLabelProvider());
		projectTreeViewer.setInput(new String());
		
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		gridData=new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		searchLabel.setLayoutData(gridData);

		gridData.horizontalAlignment = SWT.FILL;
		searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(gridData);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		final CTabFolder customTabFolder = new CTabFolder(parent, SWT.BOTTOM |  SWT.BORDER);
		GridLayout tabCompositeGridLayout = new GridLayout(1, true);
		customTabFolder.setLayout(tabCompositeGridLayout);
		gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalSpan = 1;
		gridData.horizontalSpan=2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		customTabFolder.setLayoutData(gridData);
		
		customTabFolder.setSelection(0);
		customTabFolder.setSimple(false);
		CTabItem promptTabItem = new CTabItem(customTabFolder, SWT.NULL);
		promptTabItem.setControl(createPromptTab(customTabFolder));
		promptTabItem.setText("Prompts");
		CTabItem grammarTabItem = new CTabItem(customTabFolder, SWT.NULL);
		grammarTabItem.setControl(createGrammarTab(customTabFolder));
		grammarTabItem.setText("Grammars");
		CTabItem settingsTabItem = new CTabItem(customTabFolder, SWT.NULL);
		settingsTabItem.setControl(createSettingsTab(customTabFolder));
		settingsTabItem.setText("Settings");
		this.customTabFolder=customTabFolder;
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				System.out.println(ke.keyCode+":"+ke.character+":"+ke.time);
				filter.setSearchText(searchText.getText());
				promptsTableViewer.update(projectTreeViewer.getSelection(),null);
				//promptsTableViewer.refresh();
				//grammarsTableViewer.refresh();
				//settingsTableViewer.refresh();
			}
		});
		projectTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				Object selected_Element = selection
						.getFirstElement();

				if (selected_Element != null) {
					if(customTabFolder.getSelectionIndex()==-1) customTabFolder.setSelection(0);
					promptsTableViewer.setInput(selected_Element);
					settingsTableViewer.setInput(selected_Element);
					grammarsTableViewer.setInput(selected_Element);
				}
			}

		});
		filter = new PromptsSearchFilter();
		promptsTableViewer.addFilter(filter);
		grammarsTableViewer.addFilter(filter);
		settingsTableViewer.addFilter(filter);
	}


	private Control createPromptTab(CTabFolder tabFolder) {
		final Composite parent = new Composite(tabFolder, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;

		final TableViewer tableViewer = new TableViewer(parent, SWT.NONE);
		promptsTableViewer=tableViewer;
		tableViewer.getControl().setLayoutData(gridData);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		final Menu headerMenu = new Menu(parent);
		final Menu tableMenu = new Menu(parent);
		

		final TableViewerColumn columnViewerPrompt = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerPrompt.getColumn().setText("Name");
		columnViewerPrompt.getColumn().setWidth(150);
		columnViewerPrompt.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerPrompt.getColumn());
		
		final TableViewerColumn columnViewerLang = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerLang.getColumn().setText("Language");
		columnViewerLang.getColumn().setWidth(75);
		columnViewerLang.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerLang.getColumn());
		
		
		final TableViewerColumn columnViewerBrand = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerBrand.getColumn().setText("Brand");
		columnViewerBrand.getColumn().setWidth(75);
		columnViewerBrand.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerBrand.getColumn());
		
		final TableViewerColumn columnViewerOptions = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerOptions.getColumn().setText("Options");
		columnViewerOptions.getColumn().setWidth(75);
		columnViewerOptions.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerOptions.getColumn());
		
		final TableViewerColumn columnViewerContent = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerContent.getColumn().setText("Content");
		columnViewerContent.getColumn().setWidth(150);
		createMenuItem(headerMenu, columnViewerContent.getColumn());
		
		final TableViewerColumn columnViewerContentType = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerContentType.getColumn().setText("Content Type");
		columnViewerContentType.getColumn().setWidth(0);
		columnViewerContentType.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerContentType.getColumn());

		final TableViewerColumn columnViewerScriptText = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerScriptText.getColumn().setText("Script Text");
		columnViewerScriptText.getColumn().setWidth(0);
		columnViewerScriptText.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerScriptText.getColumn());
		createTableMenu(tableMenu);
		//createTreeMenu(parent);
		
		tableViewer.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point pt = event.widget.getDisplay().map(null,
						tableViewer.getTable(), new Point(event.x, event.y));
				Rectangle clientArea = tableViewer.getTable().getClientArea();
				boolean header = clientArea.y <= pt.y
						&& pt.y < (clientArea.y + tableViewer.getTable()
								.getHeaderHeight());
				tableViewer.getTable().setMenu(header ? headerMenu : tableMenu);
			}

		});
		
		tableViewer.getTable().addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				headerMenu.dispose();
				tableMenu.dispose();
			}
		});
		tableViewer.setContentProvider(new MediaPromptsContentProvider());
		tableViewer.setLabelProvider(new MediaPromptsLabelProvider());
		return parent;

	}

	private Control createGrammarTab(CTabFolder tabFolder) {
		final Composite parent = new Composite(tabFolder, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;

		final TableViewer tableViewer = new TableViewer(parent, SWT.NONE);
		grammarsTableViewer=tableViewer;
		tableViewer.getControl().setLayoutData(gridData);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
//		final Menu headerMenu = new Menu(parent);

		final TableViewerColumn columnViewerPrompt = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerPrompt.getColumn().setText("Name");
		columnViewerPrompt.getColumn().setWidth(150);
		columnViewerPrompt.getColumn().setMoveable(false);
//		createMenuItem(headerMenu, columnViewerPrompt.getColumn());
		
		final TableViewerColumn columnViewerLang = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerLang.getColumn().setText("Language");
		columnViewerLang.getColumn().setWidth(75);
		columnViewerLang.getColumn().setMoveable(false);
//		createMenuItem(headerMenu, columnViewerLang.getColumn());
		
		
		final TableViewerColumn columnViewerBrand = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerBrand.getColumn().setText("Brand");
		columnViewerBrand.getColumn().setWidth(75);
		columnViewerBrand.getColumn().setMoveable(false);
//		createMenuItem(headerMenu, columnViewerBrand.getColumn());
		
		final TableViewerColumn columnViewerOptions = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerOptions.getColumn().setText("Options");
		columnViewerOptions.getColumn().setWidth(75);
		columnViewerOptions.getColumn().setMoveable(false);
//		createMenuItem(headerMenu, columnViewerOptions.getColumn());
		
		final TableViewerColumn columnViewerDTMFGrammar = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerDTMFGrammar.getColumn().setText("DTMF");
		columnViewerDTMFGrammar.getColumn().setWidth(250);
		
		final TableViewerColumn columnViewerVoiceGrammar = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerVoiceGrammar.getColumn().setText("Voice");
		columnViewerVoiceGrammar.getColumn().setWidth(250);
		

		final TableViewerColumn columnViewerInputMode = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerInputMode.getColumn().setToolTipText("Input Mode");
		columnViewerInputMode.getColumn().setText("Input Mode");
		columnViewerInputMode.getColumn().setWidth(75);
		
//		createMenuItem(headerMenu, columnViewerContent.getColumn());
		
//		tableViewer.getTable().addListener(SWT.MenuDetect, new Listener() {
//			@Override
//			public void handleEvent(Event event) {
//				Point pt = event.widget.getDisplay().map(null,
//						tableViewer.getTable(), new Point(event.x, event.y));
//				Rectangle clientArea = tableViewer.getTable().getClientArea();
//				boolean header = clientArea.y <= pt.y
//						&& pt.y < (clientArea.y + tableViewer.getTable()
//								.getHeaderHeight());
//				tableViewer.getTable().setMenu(header ? headerMenu : null);
//			}
//
//		});
//		
//		tableViewer.getTable().addListener(SWT.Dispose, new Listener() {
//			public void handleEvent(Event event) {
//				headerMenu.dispose();
//				// tableMenu.dispose();
//			}
//		});
		tableViewer.setContentProvider(new MediaGrammarsContentProvider());
		tableViewer.setLabelProvider(new MediaGrammarsLabelProvider());
		return parent;

	}

	private Control createSettingsTab(CTabFolder tabFolder) {
		final Composite parent = new Composite(tabFolder, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;

		final TableViewer tableViewer = new TableViewer(parent, SWT.NONE);
		settingsTableViewer=tableViewer;
		tableViewer.getControl().setLayoutData(gridData);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		final Menu headerMenu = new Menu(parent);
		final Menu tableMenu = new Menu(parent);

		final TableViewerColumn columnViewerName = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerName.getColumn().setText("Name");
		columnViewerName.getColumn().setToolTipText("Element Name");
		columnViewerName.getColumn().setWidth(150);
		columnViewerName.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerName.getColumn());
		columnViewerName.setLabelProvider(new TestLabel());
		

		final TableViewerColumn columnViewerBrand = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerBrand.getColumn().setToolTipText("Brand");
		columnViewerBrand.getColumn().setText("Brand");
		columnViewerBrand.getColumn().setWidth(75);
		columnViewerBrand.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerBrand.getColumn());

		final TableViewerColumn columnViewerVariable = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerVariable.getColumn().setToolTipText("Variable");
		columnViewerVariable.getColumn().setText("Variable");
		columnViewerVariable.getColumn().setWidth(75);
		createMenuItem(headerMenu, columnViewerVariable.getColumn());
		
		final TableViewerColumn columnViewerOptions = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerOptions.getColumn().setText("Options");
		columnViewerOptions.getColumn().setWidth(75);
		columnViewerOptions.getColumn().setMoveable(false);
		createMenuItem(headerMenu, columnViewerOptions.getColumn());
		
		final TableViewerColumn columnViewerInputMode = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerInputMode.getColumn().setToolTipText("Input Mode");
		columnViewerInputMode.getColumn().setText("Input Mode");
		columnViewerInputMode.getColumn().setWidth(75);
		createMenuItem(headerMenu, columnViewerInputMode.getColumn());
		
		final TableViewerColumn columnViewerBarge = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerBarge.getColumn().setToolTipText("Barge");
		columnViewerBarge.getColumn().setText("Barge");
		columnViewerBarge.getColumn().setWidth(0);
		columnViewerBarge.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerBarge.getColumn());
		
		final TableViewerColumn columnViewerBeep = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerBeep.getColumn().setToolTipText("Play Beep?");
		columnViewerBeep.getColumn().setText("Beep?");
		columnViewerBeep.getColumn().setWidth(0);
		columnViewerBeep.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerBeep.getColumn());
		
		final TableViewerColumn columnViewerDTMFTerm = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerDTMFTerm.getColumn().setToolTipText("Allow DTMF Temination?");
		columnViewerDTMFTerm.getColumn().setText("DTMF Termination");
		columnViewerDTMFTerm.getColumn().setWidth(0);
		columnViewerDTMFTerm.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerDTMFTerm.getColumn());
		
		final TableViewerColumn columnViewerDTMFValue = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerDTMFValue.getColumn().setToolTipText("DTMF Value");
		columnViewerDTMFValue.getColumn().setText("DTMF Value");
		columnViewerDTMFValue.getColumn().setWidth(0);
		columnViewerDTMFValue.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerDTMFValue.getColumn());
		
		final TableViewerColumn columnViewerOptionSilent = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerOptionSilent.getColumn().setToolTipText("Option is Silent?");
		columnViewerOptionSilent.getColumn().setText("Option is Silent");
		columnViewerOptionSilent.getColumn().setWidth(0);
		columnViewerOptionSilent.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerOptionSilent.getColumn());
		
		
		final TableViewerColumn columnViewerInitialTimeOut = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerInitialTimeOut.getColumn().setToolTipText("Initial Input Timeout(Sec.)");
		columnViewerInitialTimeOut.getColumn().setText("Initial Input Timeout(Sec.)");
		columnViewerInitialTimeOut.getColumn().setWidth(0);
		columnViewerInitialTimeOut.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerInitialTimeOut.getColumn());
		
		final TableViewerColumn columnViewerInterTimeOut = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerInterTimeOut.getColumn().setToolTipText("Interdigit Timeout(Sec.)");
		columnViewerInterTimeOut.getColumn().setText("Interdigit Timeout(Sec.)");
		columnViewerInterTimeOut.getColumn().setWidth(0);
		columnViewerInterTimeOut.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerInterTimeOut.getColumn());
		
		final TableViewerColumn columnViewerTermTimeOut = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerTermTimeOut.getColumn().setToolTipText("Termination Timeout(Sec.)");
		columnViewerTermTimeOut.getColumn().setText("Term Timeout(Sec.)");
		columnViewerTermTimeOut.getColumn().setWidth(0);
		columnViewerTermTimeOut.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerTermTimeOut.getColumn());
		
		final TableViewerColumn columnViewerTermChar = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerTermChar.getColumn().setToolTipText("Termination Character");
		columnViewerTermChar.getColumn().setText("Term Char");
		columnViewerTermChar.getColumn().setWidth(0);
		columnViewerTermChar.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerTermChar.getColumn());

		final TableViewerColumn columnViewerSpeechIncompleteTimeout = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerSpeechIncompleteTimeout.getColumn().setToolTipText("Speech Incompletion Timeout(Sec.)");
		columnViewerSpeechIncompleteTimeout.getColumn().setText("Speech Incompletion Timeout(Sec.)");
		columnViewerSpeechIncompleteTimeout.getColumn().setWidth(0);
		columnViewerSpeechIncompleteTimeout.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerSpeechIncompleteTimeout.getColumn());
		
		final TableViewerColumn columnViewerSpeechCompleteTimeout = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerSpeechCompleteTimeout.getColumn().setToolTipText("Speech Completion Timeout(Sec.)");
		columnViewerSpeechCompleteTimeout.getColumn().setText("Speech Completion Timeout(Sec.)");
		columnViewerSpeechCompleteTimeout.getColumn().setWidth(0);
		columnViewerSpeechCompleteTimeout.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerSpeechCompleteTimeout.getColumn());
	
		final TableViewerColumn columnViewerMaxSpeechLength = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerMaxSpeechLength.getColumn().setToolTipText("Maximum Speech Length(Sec.)");
		columnViewerMaxSpeechLength.getColumn().setText("Maximum Speech Length(Sec.)");
		columnViewerMaxSpeechLength.getColumn().setWidth(0);
		columnViewerMaxSpeechLength.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerMaxSpeechLength.getColumn());
	
		final TableViewerColumn columnViewerConfidence = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerConfidence.getColumn().setToolTipText("Minimum Confidence Level Accepted");
		columnViewerConfidence.getColumn().setText("Confidence");
		columnViewerConfidence.getColumn().setWidth(0);
		columnViewerConfidence.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerConfidence.getColumn());
		
		final TableViewerColumn columnViewerSensitivity = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerSensitivity.getColumn().setToolTipText("Typical Caller Environment");
		columnViewerSensitivity.getColumn().setText("Caller Env.");
		columnViewerSensitivity.getColumn().setWidth(0);
		columnViewerSensitivity.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerSensitivity.getColumn());
		
		final TableViewerColumn columnViewerSpeedAccuracy = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerSpeedAccuracy.getColumn().setToolTipText("Speed Vs Accuracy");
		columnViewerSpeedAccuracy.getColumn().setText("Speed Vs Accuracy");
		columnViewerSpeedAccuracy.getColumn().setWidth(0);
		columnViewerSpeedAccuracy.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerSpeedAccuracy.getColumn());
		
		
		final TableViewerColumn columnViewerMaxRecTime = new TableViewerColumn(tableViewer, SWT.NONE);
		columnViewerMaxRecTime.getColumn().setText("Maximum Recording Time(Sec.)");
		columnViewerMaxRecTime.getColumn().setWidth(0);
		columnViewerMaxRecTime.getColumn().setResizable(false);
		createMenuItem(headerMenu, columnViewerMaxRecTime.getColumn());

		tableViewer.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point pt = event.widget.getDisplay().map(null,
						tableViewer.getTable(), new Point(event.x, event.y));
				Rectangle clientArea = tableViewer.getTable().getClientArea();
				boolean header = clientArea.y <= pt.y
						&& pt.y < (clientArea.y + tableViewer.getTable()
								.getHeaderHeight());
				tableViewer.getTable().setMenu(header ? headerMenu : tableMenu);
			}

		});
		
		tableViewer.getTable().addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				headerMenu.dispose();
				 tableMenu.dispose();
			}
		});

		tableViewer.setContentProvider(new MediaSettingsContentProvider());
		tableViewer.setLabelProvider(new MediaSettingsLabelProvider());
		return parent;

	}

	

	public void createMenuItem(Menu parent, final TableColumn column) 
	{

		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		if (column.getText().equals("Name")
				|| column.getText().equals("Variable")
				||column.getText().equals("Input Mode") 
				|| column.getText().equals("Brand")
				||column.getText().equals("Options")
				||column.getText().equals("Language")
				||column.getText().equals("Options")
				||column.getText().equals("Content"))
			itemName.setEnabled(false);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(75);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}
	

	public void createTableMenu(Menu parent) 
	{	
		
		final MenuItem itemName = new MenuItem(parent, SWT.PUSH);
		itemName.setText("Refresh");
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				IStructuredSelection selection = (IStructuredSelection) projectTreeViewer.getSelection();
		Object selected_Element = selection.getFirstElement();

		if (selected_Element != null) {
			if(customTabFolder.getSelectionIndex()==-1) customTabFolder.setSelection(0);
			promptsTableViewer.setInput(selected_Element);
			settingsTableViewer.setInput(selected_Element);
			grammarsTableViewer.setInput(selected_Element);
			
		}
			
			}
		});
}
	public void createTreeMenu(Menu parent) 
	{	
		
		final MenuItem itemName = new MenuItem(parent, SWT.PUSH);
		itemName.setText("Refresh");
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				projectTreeViewer.refresh();
				promptsTableViewer.refresh();
				grammarsTableViewer.refresh();
				settingsTableViewer.refresh();
				
			}
		});
}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}