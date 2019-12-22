package de.nittka.tooling.egit.bitbucket;

import java.util.ArrayList;

class BitbucketProject {
	private String name;
	private ArrayList<BitbucketRepository> children= new ArrayList<>();

	public BitbucketProject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addChild(BitbucketRepository child) {
		children.add(child);
		child.setParent(this);
	}

	public BitbucketRepository[] getChildren() {
		return (BitbucketRepository[]) children.toArray(new BitbucketRepository[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	@Override
	public String toString() {
		return getName();
	}
}