
package fr.obeo.tools.stuart.pmi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ProjectInfo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private List<Description> description = new ArrayList<Description>();
    @SerializedName("parent_project")
    @Expose
    private List<ParentProject> parentProject = new ArrayList<ParentProject>();
    @SerializedName("bugzilla")
    @Expose
    private List<Bugzilla> bugzilla = new ArrayList<Bugzilla>();
    @SerializedName("build_url")
    @Expose
    private List<BuildUrl> buildUrl = new ArrayList<BuildUrl>();
    @SerializedName("dev_list")
    @Expose
    private List<Object> devList = new ArrayList<Object>();
    @SerializedName("documentation_url")
    @Expose
    private List<DocumentationUrl> documentationUrl = new ArrayList<DocumentationUrl>();
    @SerializedName("download_url")
    @Expose
    private List<DownloadUrl> downloadUrl = new ArrayList<DownloadUrl>();
    @SerializedName("gettingstarted_url")
    @Expose
    private List<GettingstartedUrl> gettingstartedUrl = new ArrayList<GettingstartedUrl>();
    @SerializedName("id")
    @Expose
    private List<Id> id = new ArrayList<Id>();
    @SerializedName("licenses")
    @Expose
    private List<License> licenses = new ArrayList<License>();
    @SerializedName("mailing_lists")
    @Expose
    private List<MailingList> mailingLists = new ArrayList<MailingList>();
    @SerializedName("other_links")
    @Expose
    private List<Object> otherLinks = new ArrayList<Object>();
    @SerializedName("plan_url")
    @Expose
    private List<Object> planUrl = new ArrayList<Object>();
    @SerializedName("proposal_url")
    @Expose
    private List<ProposalUrl> proposalUrl = new ArrayList<ProposalUrl>();
    @SerializedName("tags")
    @Expose
    private List<Object> tags = new ArrayList<Object>();
    @SerializedName("website_url")
    @Expose
    private List<WebsiteUrl> websiteUrl = new ArrayList<WebsiteUrl>();
    @SerializedName("wiki_url")
    @Expose
    private List<WikiUrl> wikiUrl = new ArrayList<WikiUrl>();
    @SerializedName("scope")
    @Expose
    private List<Scope> scope = new ArrayList<Scope>();
    @SerializedName("source_repo")
    @Expose
    private List<SourceRepo> sourceRepo = new ArrayList<SourceRepo>();
    @SerializedName("state")
    @Expose
    private List<State> state = new ArrayList<State>();
    @SerializedName("build_description")
    @Expose
    private List<Object> buildDescription = new ArrayList<Object>();
    @SerializedName("build_doc")
    @Expose
    private List<BuildDoc> buildDoc = new ArrayList<BuildDoc>();
    @SerializedName("build_technologies")
    @Expose
    private List<BuildTechnology> buildTechnologies = new ArrayList<BuildTechnology>();
    @SerializedName("forums")
    @Expose
    private List<Forum> forums = new ArrayList<Forum>();
    @SerializedName("logo")
    @Expose
    private List<Logo> logo = new ArrayList<Logo>();
    @SerializedName("techology_types")
    @Expose
    private List<TechologyType> techologyTypes = new ArrayList<TechologyType>();
    @SerializedName("contrib_message")
    @Expose
    private List<Object> contribMessage = new ArrayList<Object>();
    @SerializedName("downloads")
    @Expose
    private List<Download> downloads = new ArrayList<Download>();
    @SerializedName("downloads_message")
    @Expose
    private List<Object> downloadsMessage = new ArrayList<Object>();
    @SerializedName("marketplace")
    @Expose
    private List<Object> marketplace = new ArrayList<Object>();
    @SerializedName("update_sites")
    @Expose
    private List<UpdateSite> updateSites = new ArrayList<UpdateSite>();
    @SerializedName("related")
    @Expose
    private List<Related> related = new ArrayList<Related>();
    @SerializedName("team_project_sets")
    @Expose
    private List<Object> teamProjectSets = new ArrayList<Object>();
    @SerializedName("github_repos")
    @Expose
    private List<Object> githubRepos = new ArrayList<Object>();
    @SerializedName("documentation")
    @Expose
    private List<Object> documentation = new ArrayList<Object>();
    @SerializedName("working_group")
    @Expose
    private List<Object> workingGroup = new ArrayList<Object>();
    @SerializedName("releases")
    @Expose
    private List<Release> releases = new ArrayList<Release>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProjectInfo() {
    }

    /**
     * 
     * @param scope
     * @param buildTechnologies
     * @param contribMessage
     * @param gettingstartedUrl
     * @param state
     * @param downloadUrl
     * @param licenses
     * @param bugzilla
     * @param devList
     * @param downloads
     * @param related
     * @param id
     * @param title
     * @param documentationUrl
     * @param buildUrl
     * @param buildDescription
     * @param description
     * @param proposalUrl
     * @param teamProjectSets
     * @param marketplace
     * @param websiteUrl
     * @param tags
     * @param logo
     * @param forums
     * @param parentProject
     * @param otherLinks
     * @param planUrl
     * @param releases
     * @param githubRepos
     * @param sourceRepo
     * @param updateSites
     * @param workingGroup
     * @param downloadsMessage
     * @param techologyTypes
     * @param documentation
     * @param buildDoc
     * @param wikiUrl
     * @param mailingLists
     */
    public ProjectInfo(String title, List<Description> description, List<ParentProject> parentProject, List<Bugzilla> bugzilla, List<BuildUrl> buildUrl, List<Object> devList, List<DocumentationUrl> documentationUrl, List<DownloadUrl> downloadUrl, List<GettingstartedUrl> gettingstartedUrl, List<Id> id, List<License> licenses, List<MailingList> mailingLists, List<Object> otherLinks, List<Object> planUrl, List<ProposalUrl> proposalUrl, List<Object> tags, List<WebsiteUrl> websiteUrl, List<WikiUrl> wikiUrl, List<Scope> scope, List<SourceRepo> sourceRepo, List<State> state, List<Object> buildDescription, List<BuildDoc> buildDoc, List<BuildTechnology> buildTechnologies, List<Forum> forums, List<Logo> logo, List<TechologyType> techologyTypes, List<Object> contribMessage, List<Download> downloads, List<Object> downloadsMessage, List<Object> marketplace, List<UpdateSite> updateSites, List<Related> related, List<Object> teamProjectSets, List<Object> githubRepos, List<Object> documentation, List<Object> workingGroup, List<Release> releases) {
        this.title = title;
        this.description = description;
        this.parentProject = parentProject;
        this.bugzilla = bugzilla;
        this.buildUrl = buildUrl;
        this.devList = devList;
        this.documentationUrl = documentationUrl;
        this.downloadUrl = downloadUrl;
        this.gettingstartedUrl = gettingstartedUrl;
        this.id = id;
        this.licenses = licenses;
        this.mailingLists = mailingLists;
        this.otherLinks = otherLinks;
        this.planUrl = planUrl;
        this.proposalUrl = proposalUrl;
        this.tags = tags;
        this.websiteUrl = websiteUrl;
        this.wikiUrl = wikiUrl;
        this.scope = scope;
        this.sourceRepo = sourceRepo;
        this.state = state;
        this.buildDescription = buildDescription;
        this.buildDoc = buildDoc;
        this.buildTechnologies = buildTechnologies;
        this.forums = forums;
        this.logo = logo;
        this.techologyTypes = techologyTypes;
        this.contribMessage = contribMessage;
        this.downloads = downloads;
        this.downloadsMessage = downloadsMessage;
        this.marketplace = marketplace;
        this.updateSites = updateSites;
        this.related = related;
        this.teamProjectSets = teamProjectSets;
        this.githubRepos = githubRepos;
        this.documentation = documentation;
        this.workingGroup = workingGroup;
        this.releases = releases;
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

    public ProjectInfo withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    public List<Description> getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(List<Description> description) {
        this.description = description;
    }

    public ProjectInfo withDescription(List<Description> description) {
        this.description = description;
        return this;
    }

    /**
     * 
     * @return
     *     The parentProject
     */
    public List<ParentProject> getParentProject() {
        return parentProject;
    }

    /**
     * 
     * @param parentProject
     *     The parent_project
     */
    public void setParentProject(List<ParentProject> parentProject) {
        this.parentProject = parentProject;
    }

    public ProjectInfo withParentProject(List<ParentProject> parentProject) {
        this.parentProject = parentProject;
        return this;
    }

    /**
     * 
     * @return
     *     The bugzilla
     */
    public List<Bugzilla> getBugzilla() {
        return bugzilla;
    }

    /**
     * 
     * @param bugzilla
     *     The bugzilla
     */
    public void setBugzilla(List<Bugzilla> bugzilla) {
        this.bugzilla = bugzilla;
    }

    public ProjectInfo withBugzilla(List<Bugzilla> bugzilla) {
        this.bugzilla = bugzilla;
        return this;
    }

    /**
     * 
     * @return
     *     The buildUrl
     */
    public List<BuildUrl> getBuildUrl() {
        return buildUrl;
    }

    /**
     * 
     * @param buildUrl
     *     The build_url
     */
    public void setBuildUrl(List<BuildUrl> buildUrl) {
        this.buildUrl = buildUrl;
    }

    public ProjectInfo withBuildUrl(List<BuildUrl> buildUrl) {
        this.buildUrl = buildUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The devList
     */
    public List<Object> getDevList() {
        return devList;
    }

    /**
     * 
     * @param devList
     *     The dev_list
     */
    public void setDevList(List<Object> devList) {
        this.devList = devList;
    }

    public ProjectInfo withDevList(List<Object> devList) {
        this.devList = devList;
        return this;
    }

    /**
     * 
     * @return
     *     The documentationUrl
     */
    public List<DocumentationUrl> getDocumentationUrl() {
        return documentationUrl;
    }

    /**
     * 
     * @param documentationUrl
     *     The documentation_url
     */
    public void setDocumentationUrl(List<DocumentationUrl> documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public ProjectInfo withDocumentationUrl(List<DocumentationUrl> documentationUrl) {
        this.documentationUrl = documentationUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The downloadUrl
     */
    public List<DownloadUrl> getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * 
     * @param downloadUrl
     *     The download_url
     */
    public void setDownloadUrl(List<DownloadUrl> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public ProjectInfo withDownloadUrl(List<DownloadUrl> downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The gettingstartedUrl
     */
    public List<GettingstartedUrl> getGettingstartedUrl() {
        return gettingstartedUrl;
    }

    /**
     * 
     * @param gettingstartedUrl
     *     The gettingstarted_url
     */
    public void setGettingstartedUrl(List<GettingstartedUrl> gettingstartedUrl) {
        this.gettingstartedUrl = gettingstartedUrl;
    }

    public ProjectInfo withGettingstartedUrl(List<GettingstartedUrl> gettingstartedUrl) {
        this.gettingstartedUrl = gettingstartedUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The id
     */
    public List<Id> getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(List<Id> id) {
        this.id = id;
    }

    public ProjectInfo withId(List<Id> id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The licenses
     */
    public List<License> getLicenses() {
        return licenses;
    }

    /**
     * 
     * @param licenses
     *     The licenses
     */
    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public ProjectInfo withLicenses(List<License> licenses) {
        this.licenses = licenses;
        return this;
    }

    /**
     * 
     * @return
     *     The mailingLists
     */
    public List<MailingList> getMailingLists() {
        return mailingLists;
    }

    /**
     * 
     * @param mailingLists
     *     The mailing_lists
     */
    public void setMailingLists(List<MailingList> mailingLists) {
        this.mailingLists = mailingLists;
    }

    public ProjectInfo withMailingLists(List<MailingList> mailingLists) {
        this.mailingLists = mailingLists;
        return this;
    }

    /**
     * 
     * @return
     *     The otherLinks
     */
    public List<Object> getOtherLinks() {
        return otherLinks;
    }

    /**
     * 
     * @param otherLinks
     *     The other_links
     */
    public void setOtherLinks(List<Object> otherLinks) {
        this.otherLinks = otherLinks;
    }

    public ProjectInfo withOtherLinks(List<Object> otherLinks) {
        this.otherLinks = otherLinks;
        return this;
    }

    /**
     * 
     * @return
     *     The planUrl
     */
    public List<Object> getPlanUrl() {
        return planUrl;
    }

    /**
     * 
     * @param planUrl
     *     The plan_url
     */
    public void setPlanUrl(List<Object> planUrl) {
        this.planUrl = planUrl;
    }

    public ProjectInfo withPlanUrl(List<Object> planUrl) {
        this.planUrl = planUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The proposalUrl
     */
    public List<ProposalUrl> getProposalUrl() {
        return proposalUrl;
    }

    /**
     * 
     * @param proposalUrl
     *     The proposal_url
     */
    public void setProposalUrl(List<ProposalUrl> proposalUrl) {
        this.proposalUrl = proposalUrl;
    }

    public ProjectInfo withProposalUrl(List<ProposalUrl> proposalUrl) {
        this.proposalUrl = proposalUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public List<Object> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public ProjectInfo withTags(List<Object> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * 
     * @return
     *     The websiteUrl
     */
    public List<WebsiteUrl> getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * 
     * @param websiteUrl
     *     The website_url
     */
    public void setWebsiteUrl(List<WebsiteUrl> websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public ProjectInfo withWebsiteUrl(List<WebsiteUrl> websiteUrl) {
        this.websiteUrl = websiteUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The wikiUrl
     */
    public List<WikiUrl> getWikiUrl() {
        return wikiUrl;
    }

    /**
     * 
     * @param wikiUrl
     *     The wiki_url
     */
    public void setWikiUrl(List<WikiUrl> wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public ProjectInfo withWikiUrl(List<WikiUrl> wikiUrl) {
        this.wikiUrl = wikiUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The scope
     */
    public List<Scope> getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(List<Scope> scope) {
        this.scope = scope;
    }

    public ProjectInfo withScope(List<Scope> scope) {
        this.scope = scope;
        return this;
    }

    /**
     * 
     * @return
     *     The sourceRepo
     */
    public List<SourceRepo> getSourceRepo() {
        return sourceRepo;
    }

    /**
     * 
     * @param sourceRepo
     *     The source_repo
     */
    public void setSourceRepo(List<SourceRepo> sourceRepo) {
        this.sourceRepo = sourceRepo;
    }

    public ProjectInfo withSourceRepo(List<SourceRepo> sourceRepo) {
        this.sourceRepo = sourceRepo;
        return this;
    }

    /**
     * 
     * @return
     *     The state
     */
    public List<State> getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(List<State> state) {
        this.state = state;
    }

    public ProjectInfo withState(List<State> state) {
        this.state = state;
        return this;
    }

    /**
     * 
     * @return
     *     The buildDescription
     */
    public List<Object> getBuildDescription() {
        return buildDescription;
    }

    /**
     * 
     * @param buildDescription
     *     The build_description
     */
    public void setBuildDescription(List<Object> buildDescription) {
        this.buildDescription = buildDescription;
    }

    public ProjectInfo withBuildDescription(List<Object> buildDescription) {
        this.buildDescription = buildDescription;
        return this;
    }

    /**
     * 
     * @return
     *     The buildDoc
     */
    public List<BuildDoc> getBuildDoc() {
        return buildDoc;
    }

    /**
     * 
     * @param buildDoc
     *     The build_doc
     */
    public void setBuildDoc(List<BuildDoc> buildDoc) {
        this.buildDoc = buildDoc;
    }

    public ProjectInfo withBuildDoc(List<BuildDoc> buildDoc) {
        this.buildDoc = buildDoc;
        return this;
    }

    /**
     * 
     * @return
     *     The buildTechnologies
     */
    public List<BuildTechnology> getBuildTechnologies() {
        return buildTechnologies;
    }

    /**
     * 
     * @param buildTechnologies
     *     The build_technologies
     */
    public void setBuildTechnologies(List<BuildTechnology> buildTechnologies) {
        this.buildTechnologies = buildTechnologies;
    }

    public ProjectInfo withBuildTechnologies(List<BuildTechnology> buildTechnologies) {
        this.buildTechnologies = buildTechnologies;
        return this;
    }

    /**
     * 
     * @return
     *     The forums
     */
    public List<Forum> getForums() {
        return forums;
    }

    /**
     * 
     * @param forums
     *     The forums
     */
    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }

    public ProjectInfo withForums(List<Forum> forums) {
        this.forums = forums;
        return this;
    }

    /**
     * 
     * @return
     *     The logo
     */
    public List<Logo> getLogo() {
        return logo;
    }

    /**
     * 
     * @param logo
     *     The logo
     */
    public void setLogo(List<Logo> logo) {
        this.logo = logo;
    }

    public ProjectInfo withLogo(List<Logo> logo) {
        this.logo = logo;
        return this;
    }

    /**
     * 
     * @return
     *     The techologyTypes
     */
    public List<TechologyType> getTechologyTypes() {
        return techologyTypes;
    }

    /**
     * 
     * @param techologyTypes
     *     The techology_types
     */
    public void setTechologyTypes(List<TechologyType> techologyTypes) {
        this.techologyTypes = techologyTypes;
    }

    public ProjectInfo withTechologyTypes(List<TechologyType> techologyTypes) {
        this.techologyTypes = techologyTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The contribMessage
     */
    public List<Object> getContribMessage() {
        return contribMessage;
    }

    /**
     * 
     * @param contribMessage
     *     The contrib_message
     */
    public void setContribMessage(List<Object> contribMessage) {
        this.contribMessage = contribMessage;
    }

    public ProjectInfo withContribMessage(List<Object> contribMessage) {
        this.contribMessage = contribMessage;
        return this;
    }

    /**
     * 
     * @return
     *     The downloads
     */
    public List<Download> getDownloads() {
        return downloads;
    }

    /**
     * 
     * @param downloads
     *     The downloads
     */
    public void setDownloads(List<Download> downloads) {
        this.downloads = downloads;
    }

    public ProjectInfo withDownloads(List<Download> downloads) {
        this.downloads = downloads;
        return this;
    }

    /**
     * 
     * @return
     *     The downloadsMessage
     */
    public List<Object> getDownloadsMessage() {
        return downloadsMessage;
    }

    /**
     * 
     * @param downloadsMessage
     *     The downloads_message
     */
    public void setDownloadsMessage(List<Object> downloadsMessage) {
        this.downloadsMessage = downloadsMessage;
    }

    public ProjectInfo withDownloadsMessage(List<Object> downloadsMessage) {
        this.downloadsMessage = downloadsMessage;
        return this;
    }

    /**
     * 
     * @return
     *     The marketplace
     */
    public List<Object> getMarketplace() {
        return marketplace;
    }

    /**
     * 
     * @param marketplace
     *     The marketplace
     */
    public void setMarketplace(List<Object> marketplace) {
        this.marketplace = marketplace;
    }

    public ProjectInfo withMarketplace(List<Object> marketplace) {
        this.marketplace = marketplace;
        return this;
    }

    /**
     * 
     * @return
     *     The updateSites
     */
    public List<UpdateSite> getUpdateSites() {
        return updateSites;
    }

    /**
     * 
     * @param updateSites
     *     The update_sites
     */
    public void setUpdateSites(List<UpdateSite> updateSites) {
        this.updateSites = updateSites;
    }

    public ProjectInfo withUpdateSites(List<UpdateSite> updateSites) {
        this.updateSites = updateSites;
        return this;
    }

    /**
     * 
     * @return
     *     The related
     */
    public List<Related> getRelated() {
        return related;
    }

    /**
     * 
     * @param related
     *     The related
     */
    public void setRelated(List<Related> related) {
        this.related = related;
    }

    public ProjectInfo withRelated(List<Related> related) {
        this.related = related;
        return this;
    }

    /**
     * 
     * @return
     *     The teamProjectSets
     */
    public List<Object> getTeamProjectSets() {
        return teamProjectSets;
    }

    /**
     * 
     * @param teamProjectSets
     *     The team_project_sets
     */
    public void setTeamProjectSets(List<Object> teamProjectSets) {
        this.teamProjectSets = teamProjectSets;
    }

    public ProjectInfo withTeamProjectSets(List<Object> teamProjectSets) {
        this.teamProjectSets = teamProjectSets;
        return this;
    }

    /**
     * 
     * @return
     *     The githubRepos
     */
    public List<Object> getGithubRepos() {
        return githubRepos;
    }

    /**
     * 
     * @param githubRepos
     *     The github_repos
     */
    public void setGithubRepos(List<Object> githubRepos) {
        this.githubRepos = githubRepos;
    }

    public ProjectInfo withGithubRepos(List<Object> githubRepos) {
        this.githubRepos = githubRepos;
        return this;
    }

    /**
     * 
     * @return
     *     The documentation
     */
    public List<Object> getDocumentation() {
        return documentation;
    }

    /**
     * 
     * @param documentation
     *     The documentation
     */
    public void setDocumentation(List<Object> documentation) {
        this.documentation = documentation;
    }

    public ProjectInfo withDocumentation(List<Object> documentation) {
        this.documentation = documentation;
        return this;
    }

    /**
     * 
     * @return
     *     The workingGroup
     */
    public List<Object> getWorkingGroup() {
        return workingGroup;
    }

    /**
     * 
     * @param workingGroup
     *     The working_group
     */
    public void setWorkingGroup(List<Object> workingGroup) {
        this.workingGroup = workingGroup;
    }

    public ProjectInfo withWorkingGroup(List<Object> workingGroup) {
        this.workingGroup = workingGroup;
        return this;
    }

    /**
     * 
     * @return
     *     The releases
     */
    public List<Release> getReleases() {
        return releases;
    }

    /**
     * 
     * @param releases
     *     The releases
     */
    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    public ProjectInfo withReleases(List<Release> releases) {
        this.releases = releases;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
