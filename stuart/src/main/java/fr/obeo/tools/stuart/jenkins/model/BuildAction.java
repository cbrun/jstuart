package fr.obeo.tools.stuart.jenkins.model;

import java.util.Collection;

import com.google.common.collect.Lists;

public class BuildAction {
	private Collection<BuildCause> causes = Lists.newArrayList();

	private Collection<BuildParameter> parameters = Lists.newArrayList();

	private int failCount = 0;

	private int skipCount = 0;

	private int totalCount = 0;

	private String urlName = "";

	public Collection<BuildParameter> getParameters() {
		return parameters;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public void setParameters(Collection<BuildParameter> parameters) {
		this.parameters = parameters;
	}

	public Collection<BuildCause> getCauses() {
		return causes;
	}

	public void setCauses(Collection<BuildCause> causes) {
		this.causes = causes;
	}

	@Override
	public String toString() {
		return "BuildAction [causes=" + causes + ", parameters=" + parameters + "]";
	}
}
