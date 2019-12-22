package de.nittka.tooling.egit.bitbucket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

class BitbucketRepository {
	private String name;
	private String cloneURL;
	private BitbucketProject parent;

	public BitbucketRepository(JsonObject repo) {
		//TODO how to extract group
		name=repo.get("name").getAsString();
		JsonObject links = repo.get("links").getAsJsonObject();
		JsonArray cloneURLs = links.get("clone").getAsJsonArray();
		cloneURLs.forEach(urlElement->{
			JsonObject urlObj = urlElement.getAsJsonObject();
			String href = urlObj.get("href").getAsString();
			if(href.startsWith("https")) {
				cloneURL=href;
			}
		});
	}

	public BitbucketRepository(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCloneURL() {
		return cloneURL;
	}

	public void setParent(BitbucketProject parent) {
		this.parent = parent;
	}

	public BitbucketProject getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return getName()+" "+getCloneURL();
	}
}