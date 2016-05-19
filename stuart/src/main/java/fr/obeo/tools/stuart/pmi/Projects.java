
package fr.obeo.tools.stuart.pmi;

import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Projects {

	private Map<String, ProjectInfo> projects;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Projects() {
	}

	/**
	 * 
	 * @param modelingSirius
	 */
	public Projects(Map<String, ProjectInfo> modelingSirius) {
		this.projects = modelingSirius;
	}

	/**
	 * 
	 * @return The modelingSirius
	 */
	public Map<String, ProjectInfo> getProjects() {
		return projects;
	}

	/**
	 * 
	 * @param modelingSirius
	 *            The modeling.sirius
	 */
	public void setProjects(Map<String, ProjectInfo> modelingSirius) {
		this.projects = modelingSirius;
	}

	public Projects withProjects(Map<String, ProjectInfo> modelingSirius) {
		this.projects = modelingSirius;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
