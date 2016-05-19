
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Logo {

    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("filemime")
    @Expose
    private String filemime;
    @SerializedName("filesize")
    @Expose
    private String filesize;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("alt")
    @Expose
    private String alt;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Logo() {
    }

    /**
     * 
     * @param timestamp
     * @param uid
     * @param fid
     * @param title
     * @param filesize
     * @param height
     * @param status
     * @param alt
     * @param width
     * @param filename
     * @param filemime
     * @param uri
     */
    public Logo(String fid, String uid, String filename, String uri, String filemime, String filesize, String status, String timestamp, String alt, String title, String width, String height) {
        this.fid = fid;
        this.uid = uid;
        this.filename = filename;
        this.uri = uri;
        this.filemime = filemime;
        this.filesize = filesize;
        this.status = status;
        this.timestamp = timestamp;
        this.alt = alt;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    /**
     * 
     * @return
     *     The fid
     */
    public String getFid() {
        return fid;
    }

    /**
     * 
     * @param fid
     *     The fid
     */
    public void setFid(String fid) {
        this.fid = fid;
    }

    public Logo withFid(String fid) {
        this.fid = fid;
        return this;
    }

    /**
     * 
     * @return
     *     The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 
     * @param uid
     *     The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public Logo withUid(String uid) {
        this.uid = uid;
        return this;
    }

    /**
     * 
     * @return
     *     The filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 
     * @param filename
     *     The filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Logo withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * 
     * @return
     *     The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    public Logo withUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * 
     * @return
     *     The filemime
     */
    public String getFilemime() {
        return filemime;
    }

    /**
     * 
     * @param filemime
     *     The filemime
     */
    public void setFilemime(String filemime) {
        this.filemime = filemime;
    }

    public Logo withFilemime(String filemime) {
        this.filemime = filemime;
        return this;
    }

    /**
     * 
     * @return
     *     The filesize
     */
    public String getFilesize() {
        return filesize;
    }

    /**
     * 
     * @param filesize
     *     The filesize
     */
    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Logo withFilesize(String filesize) {
        this.filesize = filesize;
        return this;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Logo withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Logo withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 
     * @return
     *     The alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * 
     * @param alt
     *     The alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Logo withAlt(String alt) {
        this.alt = alt;
        return this;
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

    public Logo withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The width
     */
    public String getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    public Logo withWidth(String width) {
        this.width = width;
        return this;
    }

    /**
     * 
     * @return
     *     The height
     */
    public String getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    public Logo withHeight(String height) {
        this.height = height;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
