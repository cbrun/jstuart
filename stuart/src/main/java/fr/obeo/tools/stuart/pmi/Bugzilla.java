
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Bugzilla {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("component")
    @Expose
    private String component;
    @SerializedName("create_url")
    @Expose
    private String createUrl;
    @SerializedName("query_url")
    @Expose
    private String queryUrl;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bugzilla() {
    }

    /**
     * 
     * @param product
     * @param queryUrl
     * @param component
     * @param createUrl
     */
    public Bugzilla(String product, String component, String createUrl, String queryUrl) {
        this.product = product;
        this.component = component;
        this.createUrl = createUrl;
        this.queryUrl = queryUrl;
    }

    /**
     * 
     * @return
     *     The product
     */
    public String getProduct() {
        return product;
    }

    /**
     * 
     * @param product
     *     The product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    public Bugzilla withProduct(String product) {
        this.product = product;
        return this;
    }

    /**
     * 
     * @return
     *     The component
     */
    public String getComponent() {
        return component;
    }

    /**
     * 
     * @param component
     *     The component
     */
    public void setComponent(String component) {
        this.component = component;
    }

    public Bugzilla withComponent(String component) {
        this.component = component;
        return this;
    }

    /**
     * 
     * @return
     *     The createUrl
     */
    public String getCreateUrl() {
        return createUrl;
    }

    /**
     * 
     * @param createUrl
     *     The create_url
     */
    public void setCreateUrl(String createUrl) {
        this.createUrl = createUrl;
    }

    public Bugzilla withCreateUrl(String createUrl) {
        this.createUrl = createUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The queryUrl
     */
    public String getQueryUrl() {
        return queryUrl;
    }

    /**
     * 
     * @param queryUrl
     *     The query_url
     */
    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public Bugzilla withQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
