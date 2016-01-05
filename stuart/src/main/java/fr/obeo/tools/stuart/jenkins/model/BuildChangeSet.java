package fr.obeo.tools.stuart.jenkins.model;

import java.util.Collection;

import com.google.common.collect.Lists;

public class BuildChangeSet {

	public static final BuildChangeSet NULL = new BuildChangeSet();
	private Collection<ChangeSetItem> items = Lists.newArrayList();

	public Collection<ChangeSetItem> getItems() {
		return items;
	}

	public void setItems(Collection<ChangeSetItem> items) {
		this.items = items;
	}

}
