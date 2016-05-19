
package fr.obeo.tools.stuart.pmi;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class EndDate {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("timezone_db")
    @Expose
    private String timezoneDb;
    @SerializedName("date_type")
    @Expose
    private String dateType;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EndDate() {
    }

    /**
     * 
     * @param timezone
     * @param timezoneDb
     * @param value
     * @param dateType
     */
    public EndDate(String value, String timezone, String timezoneDb, String dateType) {
        this.value = value;
        this.timezone = timezone;
        this.timezoneDb = timezoneDb;
        this.dateType = dateType;
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

    public EndDate withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 
     * @return
     *     The timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * 
     * @param timezone
     *     The timezone
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public EndDate withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    /**
     * 
     * @return
     *     The timezoneDb
     */
    public String getTimezoneDb() {
        return timezoneDb;
    }

    /**
     * 
     * @param timezoneDb
     *     The timezone_db
     */
    public void setTimezoneDb(String timezoneDb) {
        this.timezoneDb = timezoneDb;
    }

    public EndDate withTimezoneDb(String timezoneDb) {
        this.timezoneDb = timezoneDb;
        return this;
    }

    /**
     * 
     * @return
     *     The dateType
     */
    public String getDateType() {
        return dateType;
    }

    /**
     * 
     * @param dateType
     *     The date_type
     */
    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public EndDate withDateType(String dateType) {
        this.dateType = dateType;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
