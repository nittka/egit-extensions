package de.nittka.tooling.egit.bitbucket;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

class BitbucketViewLabelProvider extends LabelProvider {
		private IWorkbench workbench = PlatformUI.getWorkbench();

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_FILE;
			if (obj instanceof BitbucketProject) {
				imageKey = ISharedImages.IMG_OBJ_PROJECT;
			}
			return workbench.getSharedImages().getImage(imageKey);
		}
	}