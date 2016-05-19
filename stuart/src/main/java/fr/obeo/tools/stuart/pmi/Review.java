
package fr.obeo.tools.stuart.pmi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Review {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private List<Description__> description = new ArrayList<Description__>();
    @SerializedName("end_date")
    @Expose
    private List<EndDate> endDate = new ArrayList<EndDate>();
    @SerializedName("links")
    @Expose
    private List<Object> links = new ArrayList<Object>();
    @SerializedName("project")
    @Expose
    private List<Project> project = new ArrayList<Project>();
    @SerializedName("reference")
    @Expose
    private List<Reference> reference = new ArrayList<Reference>();
    @SerializedName("state")
    @Expose
    private List<State_> state = new ArrayList<State_>();
    @SerializedName("top_level")
    @Expose
    private List<TopLevel> topLevel = new ArrayList<TopLevel>();
    @SerializedName("type")
    @Expose
    private List<Type_> type = new ArrayList<Type_>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Review() {
    }

    /**
     * 
     * @param project
     * @param title
     * @param description
     * @param state
     * @param links
     * @param endDate
     * @param type
     * @param topLevel
     * @param reference
     */
    public Review(String title, List<Description__> description, List<EndDate> endDate, List<Object> links, List<Project> project, List<Reference> reference, List<State_> state, List<TopLevel> topLevel, List<Type_> type) {
        this.title = title;
        this.description = description;
        this.endDate = endDate;
        this.links = links;
        this.project = project;
        this.reference = reference;
        this.state = state;
        this.topLevel = topLevel;
        this.type = type;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public Review withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    public List<Description__> getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(List<Description__> description) {
        this.description = description;
    }

    public Review withDescription(List<Description__> description) {
        this.description = description;
        return this;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public List<EndDate> getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The end_date
     */
    public void setEndDate(List<EndDate> endDate) {
        this.endDate = endDate;
    }

    public Review withEndDate(List<EndDate> endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * 
     * @return
     *     The links
     */
    public List<Object> getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The links
     */
    public void setLinks(List<Object> links) {
        this.links = links;
    }

    public Review withLinks(List<Object> links) {
        this.links = links;
        return this;
    }

    /**
     * 
     * @return
     *     The project
     */
    public List<Project> getProject() {
        return project;
    }

    /**
     * 
     * @param project
     *     The project
     */
    public void setProject(List<Project> project) {
        this.project = project;
    }

    public Review withProject(List<Project> project) {
        this.project = project;
        return this;
    }

    /**
     * 
     * @return
     *     The reference
     */
    public List<Reference> getReference() {
        return reference;
    }

    /**
     * 
     * @param reference
     *     The reference
     */
    public void setReference(List<Reference> reference) {
        this.reference = reference;
    }

    public Review withReference(List<Reference> reference) {
        this.reference = reference;
        return this;
    }

    /**
     * 
     * @return
     *     The state
     */
    public List<State_> getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(List<State_> state) {
        this.state = state;
    }

    public Review withState(List<State_> state) {
        this.state = state;
        return this;
    }

    /**
     * 
     * @return
     *     The topLevel
     */
    public List<TopLevel> getTopLevel() {
        return topLevel;
    }

    /**
     * 
     * @param topLevel
     *     The top_level
     */
    public void setTopLevel(List<TopLevel> topLevel) {
        this.topLevel = topLevel;
    }

    public Review withTopLevel(List<TopLevel> topLevel) {
        this.topLevel = topLevel;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    public List<Type_> getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(List<Type_> type) {
        this.type = type;
    }

    public Review withType(List<Type_> type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
