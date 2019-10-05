
package de.nittka.tooling.egit.mavenimport;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class ImportMavenProjectCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection instanceof IStructuredSelection) {
			Object node = ((IStructuredSelection) selection).getFirstElement();
			if (node instanceof RepositoryTreeNode<?>) {
				Repository repo = ((RepositoryTreeNode) node).getRepository();
				if (repo != null) {
					File dir = repo.getWorkTree();
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWizardDescriptor wizardDescriptor = workbench.getImportWizardRegistry()
							.findWizard("org.eclipse.m2e.core.wizards.Maven2ImportWizard");
					if (wizardDescriptor != null) {
						try {
							IWorkbenchWizard wizard = wizardDescriptor.createWizard();
							wizard.init(workbench, createSelection(dir));
							new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard).open();
							return null;
						} catch (CoreException e) {
							throw new ExecutionException("problems creating wizard", e);
						}
					} else {
						error("maven import wizard not found");
					}
				} else {
					error("no repository");
				}
			} else {
				error("wrong tree node type " + node.getClass());
			}
		} else {
			error("no selection");
		}
		return null;
	}

	private IStructuredSelection createSelection(File dir) {
		final Path path = new Path(dir.toString());
		IStructuredSelection pathSelection = new IStructuredSelection() {

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public List toList() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object[] toArray() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int size() {
				return 1;
			}

			@Override
			public Iterator iterator() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object getFirstElement() {
				return path;
			}
		};
		return pathSelection;
	}

	private void error(String error) {
		IStatus status = new Status(IStatus.ERROR, "de.nittka.tooling.egit", error);
		StatusManager.getManager().handle(status, StatusManager.SHOW);
	}
}
