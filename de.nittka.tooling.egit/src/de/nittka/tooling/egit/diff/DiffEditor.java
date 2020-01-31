package de.nittka.tooling.egit.diff;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.egit.ui.internal.commit.DiffDocument;
import org.eclipse.egit.ui.internal.commit.DiffRegionFormatter;
import org.eclipse.egit.ui.internal.commit.DiffViewer;
import org.eclipse.egit.ui.internal.commit.RepositoryCommit;
import org.eclipse.egit.ui.internal.history.FileDiff;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class DiffEditor extends AbstractTextEditor {

	private AbstractDocumentProvider dp;

	@Override
	public void createPartControl(Composite parent) {
		setSourceViewerConfiguration(new DiffViewer.Configuration(EditorsUI.getPreferenceStore()));
		super.createPartControl(parent);
	}

	@Override
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		DiffViewer diffViewer = new DiffViewer(parent, ruler, styles);
		return diffViewer;
	}

	@Override
	public IDocumentProvider getDocumentProvider() {
		if (dp == null) {
			dp = new AbstractDocumentProvider() {

				@Override
				protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
					return null;
				}

				@Override
				protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document,
						boolean overwrite) throws CoreException {
				}

				@Override
				protected IDocument createDocument(Object element) throws CoreException {
					if (element instanceof DiffEditorInput) {
						DiffEditorInput input = (DiffEditorInput) element;
						Repository repo = input.c1.getRepository();
						RepositoryCommit repoCommit = new RepositoryCommit(repo, input.c1.getRevCommit());
						FileDiff[] diffs = repoCommit.getDiffs(input.c2.getRevCommit());
						if (diffs.length == 0) {
							return new DiffDocument("Kein Unterschiede gefunden!");
						}
						final DiffDocument document = new DiffDocument();
						try (DiffRegionFormatter formatter = new DiffRegionFormatter(document, document.getLength(),
								1000000)) {
							for (FileDiff diff : diffs) {
								try {
									formatter.write(diff);
								} catch (IOException ignore) {
									// Ignored
								}
							}
							document.connect(formatter);
						}
						return document;
					}
					return new DiffDocument();
				}

				@Override
				public IDocument getDocument(Object element) {
					IDocument result = super.getDocument(element);
					return result;
				}

				@Override
				protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
					return new AnnotationModel();
				}
			};
		}
		return dp;
	}
}
