package fr.obeo.tools.stuart;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Post {

	private String key;

	private String author = "Bot";

	private String markdownBody = "";

	private String iconURL = "";

	private String subject = null;

	private Set<String> tags;
	private Set<String> urls;

	private Set<String> mediaURLs;

	private Date createdAt = new Date();

	private boolean mightTruncate = true;

	private boolean isQuote = true;

	private String projectName = null;

	private String threadID;

	private String fullHTMLBody;

	private String simpleHTMLBody;

	public String getSimpleHTMLBody() {
		return simpleHTMLBody;
	}

	public void setSimpleHTMLBody(String simpleHTMLBody) {
		this.simpleHTMLBody = simpleHTMLBody;
	}

	private Collection<PostAttachment> attachments;

	private String originalPostID;

	public String getOriginalPostID() {
		return originalPostID;
	}

	public void setOriginalPostID(String originalPostID) {
		this.originalPostID = originalPostID;
	}

	public Collection<PostAttachment> getAttachments() {
		if (attachments == null) {
			attachments = Lists.newArrayList();
		}
		return attachments;
	}

	public void setAttachments(Collection<PostAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getFullHTMLBody() {
		return fullHTMLBody;
	}

	public void setFullHTMLBody(String fullHTMLBody) {
		this.fullHTMLBody = fullHTMLBody;
	}

	public String getThreadID() {
		return threadID;
	}

	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * 
	 * @param key must be different for two differents posts. An URL or URI is a
	 *            good option here.
	 */
	public Post(String key) {
		this.key = key;
		this.tags = new LinkedHashSet<String>();
		this.urls = new LinkedHashSet<String>();
		this.mediaURLs = new LinkedHashSet<String>();
	}

	public boolean isQuote() {
		return isQuote;
	}

	public void setQuote(boolean isQuote) {
		this.isQuote = isQuote;
	}

	public String getKey() {
		return key;
	}

	public String getAuthor() {
		return author;
	}

	public String getMarkdownBody() {
		return markdownBody;
	}

	public String getIconURL() {
		return iconURL;
	}

	public String getSubject() {
		return subject;
	}

	public Collection<String> getTags() {
		return tags;
	}

	public Collection<String> getURLs() {
		return urls;
	}

	public Collection<String> getMediaURLs() {
		return mediaURLs;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Post addURLs(String... urls) {
		for (String newURL : urls) {
			this.urls.add(newURL);
		}
		return this;
	}

	public Post addMediaURLs(String... urls) {
		for (String newURL : urls) {
			this.mediaURLs.add(newURL);
		}
		return this;
	}

	@Override
	public String toString() {
		return "Post " + key + "\n[author=" + author + ", markdownBody=" + markdownBody + ", iconURL=" + iconURL
				+ ", subject=" + subject + "]";
	}

	public void mightBeTruncated(boolean value) {
		this.mightTruncate = value;
	}

	public boolean mightTruncate() {
		return this.mightTruncate;
	}

	public static Post createPost(String key, String markdownBody, String author, String iconURL, Date createdAt) {
		Post p = new Post(key);
		p.author = author;
		p.iconURL = iconURL;
		p.markdownBody = markdownBody;
		p.createdAt = createdAt;
		return p;
	}

	public static Post createPostWithSubject(String key, String subject, String markdownBody, String author,
			String iconURL, Date createdAt) {
		Post p = new Post(key);
		p.author = author;
		p.iconURL = iconURL;
		p.markdownBody = markdownBody;
		p.subject = subject;
		p.createdAt = createdAt;
		return p;
	}

}
