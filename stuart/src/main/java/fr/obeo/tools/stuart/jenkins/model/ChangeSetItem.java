package fr.obeo.tools.stuart.jenkins.model;

public class ChangeSetItem {

	private String commitId;

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public BuildAuthor getAuthor() {
		return author;
	}

	public void setAuthor(BuildAuthor author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "ChangeSetItem [commitId=" + commitId + ", author=" + author + ", comment=" + comment + ", msg=" + msg
				+ "]";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private BuildAuthor author;
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private String msg;

}
