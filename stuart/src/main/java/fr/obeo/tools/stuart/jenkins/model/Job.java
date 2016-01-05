package fr.obeo.tools.stuart.jenkins.model;

public class Job {

	private String name;

	private Build lastBuild;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Build getLastBuild() {
		return lastBuild;
	}

	public void setLastBuild(Build lastBuild) {
		this.lastBuild = lastBuild;
	}

	@Override
	public String toString() {
		return "Job [name=" + name + ", lastBuild=" + lastBuild + "]";
	}

}
