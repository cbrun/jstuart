package fr.obeo.tools.stuart.jenkins.model;

public class BuildAuthor {

	private String absoluteUrl;

	@Override
	public String toString() {
		return "BuildAuthor [absoluteUrl=" + absoluteUrl + ", fullName=" + fullName + "]";
	}

	public String getAbsoluteUrl() {
		return absoluteUrl;
	}

	public void setAbsoluteUrl(String absoluteUrl) {
		this.absoluteUrl = absoluteUrl;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	private String fullName;

}
