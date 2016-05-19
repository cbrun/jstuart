
package fr.obeo.tools.stuart.pmi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Release {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private List<Description_> description = new ArrayList<Description_>();
    @SerializedName("apis")
    @Expose
    private List<Api> apis = new ArrayList<Api>();
    @SerializedName("architecture")
    @Expose
    private List<Architecture> architecture = new ArrayList<Architecture>();
    @SerializedName("communities")
    @Expose
    private List<Community> communities = new ArrayList<Community>();
    @SerializedName("compatibility")
    @Expose
    private List<Object> compatibility = new ArrayList<Object>();
    @SerializedName("date")
    @Expose
    private List<Date> date = new ArrayList<Date>();
    @SerializedName("deliverables")
    @Expose
    private List<Object> deliverables = new ArrayList<Object>();
    @SerializedName("endoflife")
    @Expose
    private List<Endoflife> endoflife = new ArrayList<Endoflife>();
    @SerializedName("environment")
    @Expose
    private List<Object> environment = new ArrayList<Object>();
    @SerializedName("i18n")
    @Expose
    private List<Object> i18n = new ArrayList<Object>();
    @SerializedName("iplog")
    @Expose
    private List<Iplog> iplog = new ArrayList<Iplog>();
    @SerializedName("milestones")
    @Expose
    private List<Milestone> milestones = new ArrayList<Milestone>();
    @SerializedName("noncode")
    @Expose
    private List<Noncode> noncode = new ArrayList<Noncode>();
    @SerializedName("noteworthy")
    @Expose
    private List<Noteworthy> noteworthy = new ArrayList<Noteworthy>();
    @SerializedName("parent_project")
    @Expose
    private List<ParentProject_> parentProject = new ArrayList<ParentProject_>();
    @SerializedName("security")
    @Expose
    private List<Security> security = new ArrayList<Security>();
    @SerializedName("standards")
    @Expose
    private List<Standard> standards = new ArrayList<Standard>();
    @SerializedName("subprojects")
    @Expose
    private List<Object> subprojects = new ArrayList<Object>();
    @SerializedName("themes")
    @Expose
    private List<Theme> themes = new ArrayList<Theme>();
    @SerializedName("usability")
    @Expose
    private List<Usability> usability = new ArrayList<Usability>();
    @SerializedName("type")
    @Expose
    private List<Type> type = new ArrayList<Type>();
    @SerializedName("review")
    @Expose
    private Review review;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Release() {
    }

    /**
     * 
     * @param compatibility
     * @param parentProject
     * @param noteworthy
     * @param communities
     * @param apis
     * @param themes
     * @param security
     * @param architecture
     * @param type
     * @param date
     * @param iplog
     * @param usability
     * @param standards
     * @param noncode
     * @param milestones
     * @param subprojects
     * @param title
     * @param deliverables
     * @param environment
     * @param description
     * @param endoflife
     * @param i18n
     * @param review
     */
    public Release(String title, List<Description_> description, List<Api> apis, List<Architecture> architecture, List<Community> communities, List<Object> compatibility, List<Date> date, List<Object> deliverables, List<Endoflife> endoflife, List<Object> environment, List<Object> i18n, List<Iplog> iplog, List<Milestone> milestones, List<Noncode> noncode, List<Noteworthy> noteworthy, List<ParentProject_> parentProject, List<Security> security, List<Standard> standards, List<Object> subprojects, List<Theme> themes, List<Usability> usability, List<Type> type, Review review) {
        this.title = title;
        this.description = description;
        this.apis = apis;
        this.architecture = architecture;
        this.communities = communities;
        this.compatibility = compatibility;
        this.date = date;
        this.deliverables = deliverables;
        this.endoflife = endoflife;
        this.environment = environment;
        this.i18n = i18n;
        this.iplog = iplog;
        this.milestones = milestones;
        this.noncode = noncode;
        this.noteworthy = noteworthy;
        this.parentProject = parentProject;
        this.security = security;
        this.standards = standards;
        this.subprojects = subprojects;
        this.themes = themes;
        this.usability = usability;
        this.type = type;
        this.review = review;
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

    public Release withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    public List<Description_> getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(List<Description_> description) {
        this.description = description;
    }

    public Release withDescription(List<Description_> description) {
        this.description = description;
        return this;
    }

    /**
     * 
     * @return
     *     The apis
     */
    public List<Api> getApis() {
        return apis;
    }

    /**
     * 
     * @param apis
     *     The apis
     */
    public void setApis(List<Api> apis) {
        this.apis = apis;
    }

    public Release withApis(List<Api> apis) {
        this.apis = apis;
        return this;
    }

    /**
     * 
     * @return
     *     The architecture
     */
    public List<Architecture> getArchitecture() {
        return architecture;
    }

    /**
     * 
     * @param architecture
     *     The architecture
     */
    public void setArchitecture(List<Architecture> architecture) {
        this.architecture = architecture;
    }

    public Release withArchitecture(List<Architecture> architecture) {
        this.architecture = architecture;
        return this;
    }

    /**
     * 
     * @return
     *     The communities
     */
    public List<Community> getCommunities() {
        return communities;
    }

    /**
     * 
     * @param communities
     *     The communities
     */
    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public Release withCommunities(List<Community> communities) {
        this.communities = communities;
        return this;
    }

    /**
     * 
     * @return
     *     The compatibility
     */
    public List<Object> getCompatibility() {
        return compatibility;
    }

    /**
     * 
     * @param compatibility
     *     The compatibility
     */
    public void setCompatibility(List<Object> compatibility) {
        this.compatibility = compatibility;
    }

    public Release withCompatibility(List<Object> compatibility) {
        this.compatibility = compatibility;
        return this;
    }

    /**
     * 
     * @return
     *     The date
     */
    public List<Date> getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(List<Date> date) {
        this.date = date;
    }

    public Release withDate(List<Date> date) {
        this.date = date;
        return this;
    }

    /**
     * 
     * @return
     *     The deliverables
     */
    public List<Object> getDeliverables() {
        return deliverables;
    }

    /**
     * 
     * @param deliverables
     *     The deliverables
     */
    public void setDeliverables(List<Object> deliverables) {
        this.deliverables = deliverables;
    }

    public Release withDeliverables(List<Object> deliverables) {
        this.deliverables = deliverables;
        return this;
    }

    /**
     * 
     * @return
     *     The endoflife
     */
    public List<Endoflife> getEndoflife() {
        return endoflife;
    }

    /**
     * 
     * @param endoflife
     *     The endoflife
     */
    public void setEndoflife(List<Endoflife> endoflife) {
        this.endoflife = endoflife;
    }

    public Release withEndoflife(List<Endoflife> endoflife) {
        this.endoflife = endoflife;
        return this;
    }

    /**
     * 
     * @return
     *     The environment
     */
    public List<Object> getEnvironment() {
        return environment;
    }

    /**
     * 
     * @param environment
     *     The environment
     */
    public void setEnvironment(List<Object> environment) {
        this.environment = environment;
    }

    public Release withEnvironment(List<Object> environment) {
        this.environment = environment;
        return this;
    }

    /**
     * 
     * @return
     *     The i18n
     */
    public List<Object> getI18n() {
        return i18n;
    }

    /**
     * 
     * @param i18n
     *     The i18n
     */
    public void setI18n(List<Object> i18n) {
        this.i18n = i18n;
    }

    public Release withI18n(List<Object> i18n) {
        this.i18n = i18n;
        return this;
    }

    /**
     * 
     * @return
     *     The iplog
     */
    public List<Iplog> getIplog() {
        return iplog;
    }

    /**
     * 
     * @param iplog
     *     The iplog
     */
    public void setIplog(List<Iplog> iplog) {
        this.iplog = iplog;
    }

    public Release withIplog(List<Iplog> iplog) {
        this.iplog = iplog;
        return this;
    }

    /**
     * 
     * @return
     *     The milestones
     */
    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * 
     * @param milestones
     *     The milestones
     */
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

    public Release withMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    /**
     * 
     * @return
     *     The noncode
     */
    public List<Noncode> getNoncode() {
        return noncode;
    }

    /**
     * 
     * @param noncode
     *     The noncode
     */
    public void setNoncode(List<Noncode> noncode) {
        this.noncode = noncode;
    }

    public Release withNoncode(List<Noncode> noncode) {
        this.noncode = noncode;
        return this;
    }

    /**
     * 
     * @return
     *     The noteworthy
     */
    public List<Noteworthy> getNoteworthy() {
        return noteworthy;
    }

    /**
     * 
     * @param noteworthy
     *     The noteworthy
     */
    public void setNoteworthy(List<Noteworthy> noteworthy) {
        this.noteworthy = noteworthy;
    }

    public Release withNoteworthy(List<Noteworthy> noteworthy) {
        this.noteworthy = noteworthy;
        return this;
    }

    /**
     * 
     * @return
     *     The parentProject
     */
    public List<ParentProject_> getParentProject() {
        return parentProject;
    }

    /**
     * 
     * @param parentProject
     *     The parent_project
     */
    public void setParentProject(List<ParentProject_> parentProject) {
        this.parentProject = parentProject;
    }

    public Release withParentProject(List<ParentProject_> parentProject) {
        this.parentProject = parentProject;
        return this;
    }

    /**
     * 
     * @return
     *     The security
     */
    public List<Security> getSecurity() {
        return security;
    }

    /**
     * 
     * @param security
     *     The security
     */
    public void setSecurity(List<Security> security) {
        this.security = security;
    }

    public Release withSecurity(List<Security> security) {
        this.security = security;
        return this;
    }

    /**
     * 
     * @return
     *     The standards
     */
    public List<Standard> getStandards() {
        return standards;
    }

    /**
     * 
     * @param standards
     *     The standards
     */
    public void setStandards(List<Standard> standards) {
        this.standards = standards;
    }

    public Release withStandards(List<Standard> standards) {
        this.standards = standards;
        return this;
    }

    /**
     * 
     * @return
     *     The subprojects
     */
    public List<Object> getSubprojects() {
        return subprojects;
    }

    /**
     * 
     * @param subprojects
     *     The subprojects
     */
    public void setSubprojects(List<Object> subprojects) {
        this.subprojects = subprojects;
    }

    public Release withSubprojects(List<Object> subprojects) {
        this.subprojects = subprojects;
        return this;
    }

    /**
     * 
     * @return
     *     The themes
     */
    public List<Theme> getThemes() {
        return themes;
    }

    /**
     * 
     * @param themes
     *     The themes
     */
    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public Release withThemes(List<Theme> themes) {
        this.themes = themes;
        return this;
    }

    /**
     * 
     * @return
     *     The usability
     */
    public List<Usability> getUsability() {
        return usability;
    }

    /**
     * 
     * @param usability
     *     The usability
     */
    public void setUsability(List<Usability> usability) {
        this.usability = usability;
    }

    public Release withUsability(List<Usability> usability) {
        this.usability = usability;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    public List<Type> getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(List<Type> type) {
        this.type = type;
    }

    public Release withType(List<Type> type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The review
     */
    public Review getReview() {
        return review;
    }

    /**
     * 
     * @param review
     *     The review
     */
    public void setReview(Review review) {
        this.review = review;
    }

    public Release withReview(Review review) {
        this.review = review;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
