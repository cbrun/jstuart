
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Description_ {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("safe_value")
    @Expose
    private String safeValue;
    @SerializedName("safe_summary")
    @Expose
    private String safeSummary;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Description_() {
    }

    /**
     * 
     * @param summary
     * @param safeSummary
     * @param safeValue
     * @param value
     * @param format
     */
    public Description_(String value, String summary, String format, String safeValue, String safeSummary) {
        this.value = value;
        this.summary = summary;
        this.format = format;
        this.safeValue = safeValue;
        this.safeSummary = safeSummary;
    }

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public Description_ withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 
     * @return
     *     The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 
     * @param summary
     *     The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Description_ withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * 
     * @return
     *     The format
     */
    public String getFormat() {
        return format;
    }

    /**
     * 
     * @param format
     *     The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public Description_ withFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 
     * @return
     *     The safeValue
     */
    public String getSafeValue() {
        return safeValue;
    }

    /**
     * 
     * @param safeValue
     *     The safe_value
     */
    public void setSafeValue(String safeValue) {
        this.safeValue = safeValue;
    }

    public Description_ withSafeValue(String safeValue) {
        this.safeValue = safeValue;
        return this;
    }

    /**
     * 
     * @return
     *     The safeSummary
     */
    public String getSafeSummary() {
        return safeSummary;
    }

    /**
     * 
     * @param safeSummary
     *     The safe_summary
     */
    public void setSafeSummary(String safeSummary) {
        this.safeSummary = safeSummary;
    }

    public Description_ withSafeSummary(String safeSummary) {
        this.safeSummary = safeSummary;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
