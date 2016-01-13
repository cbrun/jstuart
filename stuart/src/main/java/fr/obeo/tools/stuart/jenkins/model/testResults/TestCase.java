package fr.obeo.tools.stuart.jenkins.model.testResults;

public class TestCase {
	private int age = 0;
	private String className;
	private float duration;
	private int failedSince;
	private String name;
	private boolean skipped = false;
	private String status;

	public int getAge() {
		return age;
	}

	public String getClassName() {
		return className;
	}

	public double getDuration() {
		return duration;
	}

	public int getFailedSince() {
		return failedSince;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "TestCase [age=" + age + ", className=" + className + ", duration=" + duration + ", failedSince="
				+ failedSince + ", name=" + name + ", skipped=" + skipped + ", status=" + status + "]";
	}

	public boolean isSkipped() {
		return skipped;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void setFailedSince(int failedSince) {
		this.failedSince = failedSince;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
