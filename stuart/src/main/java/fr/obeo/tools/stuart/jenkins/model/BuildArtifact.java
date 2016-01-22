package fr.obeo.tools.stuart.jenkins.model;

public class BuildArtifact {

	private String displayPath;

	private String fileName;

	private String relativePath;

	public String getDisplayPath() {
		return displayPath;
	}

	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "BuildArtifact [displayPath=" + displayPath + ", fileName=" + fileName + ", relativePath=" + relativePath
				+ "]";
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

}
