
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Endoflife {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("safe_value")
    @Expose
    private String safeValue;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Endoflife() {
    }

    /**
     * 
     * @param safeValue
     * @param value
     * @param format
     */
    public Endoflife(String value, String format, String safeValue) {
        this.value = value;
        this.format = format;
        this.safeValue = safeValue;
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

    public Endoflife withValue(String value) {
        this.value = value;
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

    public Endoflife withFormat(String format) {
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

    public Endoflife withSafeValue(String safeValue) {
        this.safeValue = safeValue;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
