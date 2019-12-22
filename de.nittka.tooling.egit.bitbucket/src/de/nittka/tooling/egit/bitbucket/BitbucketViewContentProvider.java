package de.nittka.tooling.egit.bitbucket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class BitbucketViewContentProvider implements ITreeContentProvider {
	/**
	 *
	 */
	private final BitbucketView bitbucketView;

	/**
	 * @param bitbucketView
	 */
	BitbucketViewContentProvider(BitbucketView bitbucketView) {
		this.bitbucketView = bitbucketView;
	}

	private List<BitbucketProject> projects;

	public Object[] getElements(Object parent) {
		if (parent.equals(this.bitbucketView.getViewSite())) {
			if (projects == null)
				initialize();
			return projects.toArray();
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof BitbucketRepository) {
			return ((BitbucketRepository) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof BitbucketProject) {
			return ((BitbucketProject) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof BitbucketProject)
			return ((BitbucketProject) parent).hasChildren();
		return false;
	}

	private void initialize() {
		List<BitbucketRepository> repos=new ArrayList<BitbucketRepository>();
		try {
			//TODO Api-URL; am besten als änderbares Memento im View
			//TODO extract project
			URL bitbucketURL = new URL("https://api.bitbucket.org/2.0/repositories?pagelen=20");
			try(InputStreamReader reader = new InputStreamReader(bitbucketURL.openStream())){
				JsonParser parser = new JsonParser();
				JsonElement json = parser.parse(reader);
				JsonObject obj = json.getAsJsonObject();
				JsonArray elements = obj.get("values").getAsJsonArray();
				elements.forEach(e->{
					JsonObject repo=e.getAsJsonObject();
					if("git".equals(repo.get("scm").getAsString())){
						repos.add(new BitbucketRepository(repo));
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}

		projects=new ArrayList<>();
		BitbucketProject p1 = new BitbucketProject("Project");
		repos.forEach(r->p1.addChild(r));
		projects.add(p1);
	}
}