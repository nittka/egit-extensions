package de.nittka.tooling.egit.diff;

import org.eclipse.egit.core.internal.IRepositoryCommit;
import org.eclipse.egit.ui.internal.UIIcons;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

public class DiffEditorInput implements IEditorInput, IPersistableElement {

	IRepositoryCommit c1;
	IRepositoryCommit c2;

	public DiffEditorInput(IRepositoryCommit c1, IRepositoryCommit c2) {
		this.c1=c1;
		this.c2=c2;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return UIIcons.CHANGESET;
	}

	@Override
	public String getName() {
		return "Diff "+c2.getRevCommit().abbreviate(6).name()+"->"+c1.getRevCommit().abbreviate(6).name();
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return null;
	}

	@Override
	public void saveState(IMemento memento) {
	}

	@Override
	public String getFactoryId() {
		return "";
	}

}
