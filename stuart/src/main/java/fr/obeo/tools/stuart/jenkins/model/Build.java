package fr.obeo.tools.stuart.jenkins.model;

public class Build {

	private boolean building = false;

	public boolean isBuilding() {
		return building;
	}

	@Override
	public String toString() {
		return "Build [building=" + building + ", timestamp=" + timestamp + ", url=" + url + "]";
	}

	public void setBuilding(boolean building) {
		this.building = building;
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

	private long timestamp;

	private String url;

}
