package fr.obeo.tools.stuart.jenkins.model.testResults;

import java.util.Collection;

import com.google.common.collect.Lists;

public class TestSuite {

	private Collection<TestCase> cases = Lists.newArrayList();

	public Collection<TestCase> getCases() {
		return cases;
	}

	public void setCases(Collection<TestCase> cases) {
		this.cases = cases;
	}

	@Override
	public String toString() {
		return "TestSuite [cases=" + cases + "]";
	}

}
