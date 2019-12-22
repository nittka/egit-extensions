package de.nittka.tooling.egit.bitbucket;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class RepoNameFilterContribution extends ControlContribution {

		private Text text;

		ViewerFilter filter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if(text!=null && text.getText()!=null) {
					String subString=text.getText().toLowerCase();
					if(element instanceof BitbucketRepository) {
						String name=((BitbucketRepository) element).getName();
						if(!contains(name, subString)) {
							return false;
						}
					}
					if(element instanceof BitbucketProject) {
						for(BitbucketRepository repo: ((BitbucketProject) element).getChildren()) {
							if(contains(repo.getName(), subString)) {
								return true;
							}
						}
						return false;
					}
				}
				return true;
			}

			private boolean contains(String name, String subString) {
				return name.toLowerCase().contains(subString);
			}
		};

		private TreeViewer viewer;

		public RepoNameFilterContribution(TreeViewer viewer) {
			super("de.nittka.tooling.egit.bitbucketview.repoNameFilterField"); //$NON-NLS-1$
			this.viewer=viewer;
			viewer.setFilters(filter);
		}

		@Override
		protected int computeWidth(Control control) {
			return control.computeSize(100, SWT.DEFAULT).x;
		}


		@Override
		protected Control createControl(Composite parent) {
			text = new Text(parent, SWT.SINGLE | SWT.BORDER);
			text.setToolTipText("Filter Repository-Name");
			text.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					update();
				}

			});
			return text;
		}

		@Override
		public void update() {
			if(text!=null && text.getText()!=null && text.getText().length()>0) {
				viewer.expandAll();
			}
			viewer.refresh();
		}

	}