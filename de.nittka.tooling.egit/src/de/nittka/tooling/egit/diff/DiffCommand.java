
package de.nittka.tooling.egit.diff;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.egit.core.internal.IRepositoryCommit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.statushandlers.StatusManager;

public class DiffCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection instanceof IStructuredSelection) {
			List<?> list = ((IStructuredSelection) selection).toList();
			if(list.size()==2) {
				Object c1 = list.get(0);
				Object c2 = list.get(1);
				if(c1 instanceof IRepositoryCommit && c2 instanceof IRepositoryCommit) {
					showDiff((IRepositoryCommit)c1,(IRepositoryCommit) c2);
				}
			}
		}
		return null;
	}

	private void showDiff(IRepositoryCommit c1, IRepositoryCommit c2) {
		DiffEditorInput editorInuput = new DiffEditorInput(c1, c2);
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			IDE.openEditor(page, editorInuput, "de.nittka.tooling.egit.diffeditor");
		} catch (PartInitException e) {
			IStatus status = new Status(IStatus.ERROR, "de.nittka.tooling.egit", "Error opening diff view", e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
	}
}
