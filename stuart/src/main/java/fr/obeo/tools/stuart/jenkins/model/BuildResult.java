package fr.obeo.tools.stuart.jenkins.model;

import java.util.Collection;

import com.google.common.collect.Lists;

public class BuildResult {

	private boolean building;

	private String fullDisplayName;

	private long duration;

	private String id;

	private boolean keepLog;

	private String result;

	private long timestamp;

	public boolean isBuilding() {
		return building;
	}

	public void setBuilding(boolean building) {
		this.building = building;
	}

	public String getFullDisplayName() {
		return fullDisplayName;
	}

	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isKeepLog() {
		return keepLog;
	}

	public void setKeepLog(boolean keepLog) {
		this.keepLog = keepLog;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String url;

	public Collection<BuildAction> getActions() {
		return actions;
	}

	public void setActions(Collection<BuildAction> actions) {
		this.actions = actions;
	}

	private Collection<BuildAction> actions = Lists.newArrayList();

	private Collection<BuildAuthor> culprits = Lists.newArrayList();

	private Collection<BuildArtifact> artifacts = Lists.newArrayList();

	public Collection<BuildArtifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Collection<BuildArtifact> artifacts) {
		this.artifacts = artifacts;
	}

	public Collection<BuildAuthor> getCulprits() {
		return culprits;
	}

	public void setCulprits(Collection<BuildAuthor> culprits) {
		this.culprits = culprits;
	}

	private BuildChangeSet changeSet = BuildChangeSet.NULL;

	@Override
	public String toString() {
		return "BuildResult [building=" + building + ", fullDisplayName=" + fullDisplayName + ", duration=" + duration
				+ ", id=" + id + ", keepLog=" + keepLog + ", result=" + result + ", timestamp=" + timestamp + ", url="
				+ url + ", actions=" + actions + "]";
	}

	public BuildChangeSet getChangeSet() {
		return changeSet;
	}

	public void setChangeSet(BuildChangeSet changeSet) {
		this.changeSet = changeSet;
	}
}
