
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Iplog {

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
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Iplog() {
    }

    /**
     * 
     * @param timestamp
     * @param uid
     * @param fid
     * @param filesize
     * @param status
     * @param description
     * @param filename
     * @param display
     * @param filemime
     * @param uri
     */
    public Iplog(String fid, String uid, String filename, String uri, String filemime, String filesize, String status, String timestamp, String display, String description) {
        this.fid = fid;
        this.uid = uid;
        this.filename = filename;
        this.uri = uri;
        this.filemime = filemime;
        this.filesize = filesize;
        this.status = status;
        this.timestamp = timestamp;
        this.display = display;
        this.description = description;
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

    public Iplog withFid(String fid) {
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

    public Iplog withUid(String uid) {
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

    public Iplog withFilename(String filename) {
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

    public Iplog withUri(String uri) {
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

    public Iplog withFilemime(String filemime) {
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

    public Iplog withFilesize(String filesize) {
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

    public Iplog withStatus(String status) {
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

    public Iplog withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 
     * @return
     *     The display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * 
     * @param display
     *     The display
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    public Iplog withDisplay(String display) {
        this.display = display;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Iplog withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
