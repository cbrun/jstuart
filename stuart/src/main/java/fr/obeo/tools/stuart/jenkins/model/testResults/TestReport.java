package fr.obeo.tools.stuart.jenkins.model.testResults;

import java.util.Collection;

import com.google.common.collect.Lists;

public class TestReport {

	private Collection<ChildReport> childReports = Lists.newArrayList();

	private float duration;

	private int failCount;

	private int passCount;

	private int skipCount;

	private Collection<TestSuite> suites = Lists.newArrayList();

	private int totalCount;

	public Collection<ChildReport> getChildReports() {
		return childReports;
	}

	@Override
	public String toString() {
		return "TestReport [childReports=" + childReports + ", duration=" + duration + ", failCount=" + failCount
				+ ", passCount=" + passCount + ", skipCount=" + skipCount + ", suites=" + suites + ", totalCount="
				+ totalCount + "]";
	}

	public double getDuration() {
		return duration;
	}

	public int getFailCount() {
		return failCount;
	}

	public int getPassCount() {
		return passCount;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public Collection<TestSuite> getSuites() {
		return suites;
	}

	public int getTotalCount() {
		if (totalCount == 0 && (failCount > 0 || passCount > 0 || skipCount > 0)) {
			return failCount + passCount + skipCount;
		}
		return totalCount;
	}

	public void setChildReports(Collection<ChildReport> childReports) {
		this.childReports = childReports;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public void setSuites(Collection<TestSuite> suites) {
		this.suites = suites;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
