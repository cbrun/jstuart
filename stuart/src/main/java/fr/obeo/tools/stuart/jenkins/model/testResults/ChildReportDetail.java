package fr.obeo.tools.stuart.jenkins.model.testResults;

public class ChildReportDetail {

	private int number;

	private String url;

	public int getNumber() {
		return number;
	}

	public String getUrl() {
		return url;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ChildReportDetail [number=" + number + ", url=" + url + "]";
	}
}
