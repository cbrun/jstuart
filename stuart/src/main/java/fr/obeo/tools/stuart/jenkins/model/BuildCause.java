package fr.obeo.tools.stuart.jenkins.model;

public class BuildCause {

	private String shortDescription = "";

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Override
	public String toString() {
		return "BuildCause [shortDescription=" + shortDescription + "]";
	}

}
