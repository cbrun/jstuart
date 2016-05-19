
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class BuildTechnology {

    @SerializedName("tid")
    @Expose
    private String tid;

    /**
     * No args constructor for use in serialization
     * 
     */
    public BuildTechnology() {
    }

    /**
     * 
     * @param tid
     */
    public BuildTechnology(String tid) {
        this.tid = tid;
    }

    /**
     * 
     * @return
     *     The tid
     */
    public String getTid() {
        return tid;
    }

    /**
     * 
     * @param tid
     *     The tid
     */
    public void setTid(String tid) {
        this.tid = tid;
    }

    public BuildTechnology withTid(String tid) {
        this.tid = tid;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
