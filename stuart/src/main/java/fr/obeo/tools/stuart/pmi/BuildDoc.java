
package fr.obeo.tools.stuart.pmi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class BuildDoc {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("attributes")
    @Expose
    private List<Object> attributes = new ArrayList<Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public BuildDoc() {
    }

    /**
     * 
     * @param title
     * @param attributes
     * @param url
     */
    public BuildDoc(String url, Object title, List<Object> attributes) {
        this.url = url;
        this.title = title;
        this.attributes = attributes;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public BuildDoc withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 
     * @return
     *     The title
     */
    public Object getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(Object title) {
        this.title = title;
    }

    public BuildDoc withTitle(Object title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The attributes
     */
    public List<Object> getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    public BuildDoc withAttributes(List<Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
