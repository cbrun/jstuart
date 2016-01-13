package fr.obeo.tools.stuart.jenkins.model.testResults;

public class ChildReport {
	private ChildReportDetail child;

	private TestReport result;

	public ChildReportDetail getChild() {
		return child;
	}

	public TestReport getResult() {
		return result;
	}

	public void setChild(ChildReportDetail child) {
		this.child = child;
	}

	public void setResult(TestReport result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ChildReport [child=" + child + ", result=" + result + "]";
	}

}
