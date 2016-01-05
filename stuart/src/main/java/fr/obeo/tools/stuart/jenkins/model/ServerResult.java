package fr.obeo.tools.stuart.jenkins.model;

import java.util.Collection;

import com.google.common.collect.Lists;

public class ServerResult {

	private Collection<Job> jobs = Lists.newArrayList();

	public Collection<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}

	@Override
	public String toString() {
		return "ServerResult [jobs=" + jobs + "]";
	}
}
