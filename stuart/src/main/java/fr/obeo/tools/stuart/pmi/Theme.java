
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Theme {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("committed")
    @Expose
    private String committed;
    @SerializedName("proposed")
    @Expose
    private String proposed;
    @SerializedName("deferred")
    @Expose
    private String deferred;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Theme() {
    }

    /**
     * 
     * @param committed
     * @param description
     * @param name
     * @param proposed
     * @param deferred
     */
    public Theme(String name, String description, String committed, String proposed, String deferred) {
        this.name = name;
        this.description = description;
        this.committed = committed;
        this.proposed = proposed;
        this.deferred = deferred;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Theme withName(String name) {
        this.name = name;
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

    public Theme withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 
     * @return
     *     The committed
     */
    public String getCommitted() {
        return committed;
    }

    /**
     * 
     * @param committed
     *     The committed
     */
    public void setCommitted(String committed) {
        this.committed = committed;
    }

    public Theme withCommitted(String committed) {
        this.committed = committed;
        return this;
    }

    /**
     * 
     * @return
     *     The proposed
     */
    public String getProposed() {
        return proposed;
    }

    /**
     * 
     * @param proposed
     *     The proposed
     */
    public void setProposed(String proposed) {
        this.proposed = proposed;
    }

    public Theme withProposed(String proposed) {
        this.proposed = proposed;
        return this;
    }

    /**
     * 
     * @return
     *     The deferred
     */
    public String getDeferred() {
        return deferred;
    }

    /**
     * 
     * @param deferred
     *     The deferred
     */
    public void setDeferred(String deferred) {
        this.deferred = deferred;
    }

    public Theme withDeferred(String deferred) {
        this.deferred = deferred;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
