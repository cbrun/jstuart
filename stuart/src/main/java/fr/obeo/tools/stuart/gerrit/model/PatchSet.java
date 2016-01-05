package fr.obeo.tools.stuart.gerrit.model;

import java.util.Date;

public class PatchSet {

	private String branch;

	private String changeId;
	private Date created;

	private int deletions;

	private String id;

	private int insertions;

	private PatchSetOwner owner;

	private String patchset;

	private String status;

	private String subject;

	private int _number;

	public int get_number() {
		return _number;
	}

	public void set_number(int number) {
		this._number = number;
	}

	private Date updated;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isMergeable() {
		return mergeable;
	}

	public void setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
	}

	private boolean mergeable;

	public String getBranch() {
		return branch;
	}

	public String getChangeId() {
		return changeId;
	}

	public Date getCreated() {
		return created;
	}

	public int getDeletions() {
		return deletions;
	}

	public String getId() {
		return id;
	}

	public int getInsertions() {
		return insertions;
	}

	public PatchSetOwner getOwner() {
		return owner;
	}

	public String getPatchset() {
		return patchset;
	}

	public String getStatus() {
		return status;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInsertions(int insertionns) {
		this.insertions = insertionns;
	}

	public void setOwner(PatchSetOwner owner) {
		this.owner = owner;
	}

	public void setPatchset(String patchset) {
		this.patchset = patchset;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "PatchSet [id=" + id + ", patchset=" + patchset + ", branch=" + branch + ", changeId=" + changeId
				+ ", created=" + created + ", updated=" + updated + ", insertions=" + insertions + ", deletions="
				+ deletions + ", owner=" + owner + "]";
	}
}
