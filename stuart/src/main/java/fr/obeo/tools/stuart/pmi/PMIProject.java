
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PMIProject {

    @SerializedName("projects")
    @Expose
    private Projects projects;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PMIProject() {
    }

    /**
     * 
     * @param projects
     */
    public PMIProject(Projects projects) {
        this.projects = projects;
    }

    /**
     * 
     * @return
     *     The projects
     */
    public Projects getProjects() {
        return projects;
    }

    /**
     * 
     * @param projects
     *     The projects
     */
    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public PMIProject withProjects(Projects projects) {
        this.projects = projects;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
