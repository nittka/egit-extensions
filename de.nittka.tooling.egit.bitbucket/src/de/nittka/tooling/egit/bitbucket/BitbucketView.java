package de.nittka.tooling.egit.bitbucket;

import org.eclipse.egit.ui.internal.clone.GitCloneWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

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

public class BitbucketView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.nittka.tooling.egit.bitbucket.BitbucketView";

	private TreeViewer viewer;
	private Action cloneAction;
	private Action collapseAction;
	private Action refreshAction;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new BitbucketViewContentProvider(this));
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new BitbucketViewLabelProvider());
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				BitbucketView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}


	private void fillContextMenu(IMenuManager manager) {
		if(getSelectedRepository()!=null) {
			manager.add(cloneAction);
		}
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new RepoNameFilterContribution(viewer));
		manager.add(collapseAction);
//		manager.add(refreshAction);
	}

	private void makeActions() {
		cloneAction = new Action() {
			public void run() {
				runCloneAction();
			}
		};
		cloneAction.setText("Clone Repository");
		cloneAction.setToolTipText("Clone the selected Repository");

		collapseAction = new Action() {
			public void run() {
				viewer.collapseAll();
			}
		};
		collapseAction.setToolTipText("Collapse all");
		collapseAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));
		refreshAction= new Action() {
			public void run() {
				InputDialog dialog = new InputDialog(getSite().getShell(), "Retrieve Repositories", "Enter the base URL of the bitbucket API URL", "<baseURL", null);
				if(dialog.open()==InputDialog.OK) {
					//TODO retrieve, store in memento, update content-Provider
				}
			};
		};
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Copy Repository information from Bitbucket");
		refreshAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
	}

	private BitbucketRepository getSelectedRepository() {
		ISelection selection = getSite().getService(ISelectionService.class).getSelection();
		if(selection instanceof StructuredSelection) {
			Object node = ((StructuredSelection) selection).getFirstElement();
			if(node instanceof BitbucketRepository) {
				return (BitbucketRepository)node;
			}
		}
		return null;
	}

	private void runCloneAction() {
		BitbucketRepository repo = getSelectedRepository();
		if(repo!=null) {
			GitCloneWizard wizard =new GitCloneWizard(repo.getCloneURL());
			wizard.setShowProjectImport(true);
			WizardDialog dlg = new WizardDialog(getViewSite().getShell(), wizard);
			dlg.setHelpAvailable(true);
			dlg.open();
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
